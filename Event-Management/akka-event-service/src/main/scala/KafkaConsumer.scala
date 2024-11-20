import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import java.io.{BufferedWriter, FileWriter}
import spray.json._
import JsonFormats._

object EventManagementMsgReceivers {
  val CATERING = "CATERING"
  val ENTERTAINMENT = "ENTERTAINMENT"
  val DECORATIONS = "DECORATIONS"
  val LOGISTICS = "LOGISTICS"
  val MANAGER = "MANAGER"
}

object MessageTopics {
  val EVENT_MANAGEMENT_TOPIC = "event-management-topic"
}

class EventManagementNotificationWriterActor() extends Actor {
  def receive: Receive = {
    case (to:String,messageType: String, message: String) =>
      println(s"Sending mail to $to")
      println("Message")
  }
}

class CateringMessageListener(notificationWriterActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Catering Message Listener consumes the message")
      notificationWriterActor ! ("CateringServiceTeam", msg.messageType, msg.message)
  }
}

class EntertainmentMessageListener(notificationWriterActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Entertainment Message Listener consumes the message")
      notificationWriterActor ! ("EntertainmentServiceTeam", msg.messageType, msg.message)
  }
}

class DecorationMessageListener(notificationWriterActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Decoration Message Listener consumes the message")
      notificationWriterActor ! ("DecorationServiceTeam", msg.messageType, msg.message)
  }
}

class LogisticsMessageListener(notificationWriterActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Logistics Message Listener consumes the message")
      notificationWriterActor ! ("LogisticsServiceTeam", msg.messageType, msg.message)
  }
}

class ManagerMessageListener(notificationWriterActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Manager Message Listener consumes the message")
      notificationWriterActor ! ("Manager", msg.messageType, msg.message)
  }
}

class EventManagementListener(cateringMessageListener: ActorRef,
                              entertainmentMessageListener: ActorRef,
                              decorationMessageListener: ActorRef,
                              logisticsMessageListener: ActorRef,
                              managerMessageListener: ActorRef
                             )extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat => msg.receiver match {
      case EventManagementMsgReceivers.CATERING =>
        cateringMessageListener ! msg
      case EventManagementMsgReceivers.ENTERTAINMENT =>
        entertainmentMessageListener ! msg
      case EventManagementMsgReceivers.DECORATIONS =>
        decorationMessageListener ! msg
      case EventManagementMsgReceivers.LOGISTICS =>
        logisticsMessageListener ! msg
      case EventManagementMsgReceivers.MANAGER =>
        managerMessageListener ! msg
    }
  }

}

object KafkaConsumer {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("MessagingConsumerSystem")

    val emnotificationWriterActor: ActorRef = system.actorOf(Props(new EventManagementNotificationWriterActor), "EventManagementnotificationWriterActor")

    // Create the actors for all the event management listeners
    val cateringMessageListener: ActorRef = system.actorOf(Props(new CateringMessageListener(emnotificationWriterActor)), "CateringMessageListener")
    val entertainmentMessageListener: ActorRef = system.actorOf(Props(new EntertainmentMessageListener(emnotificationWriterActor)), "EntertainmentMessageListener")
    val decorationsMessageListener: ActorRef = system.actorOf(Props(new DecorationMessageListener(emnotificationWriterActor)), "DecorationMessageListener")
    val logisticsMessageListener: ActorRef = system.actorOf(Props(new LogisticsMessageListener(emnotificationWriterActor)), "LogisticsMessageListener")
    val managerMessageListener: ActorRef = system.actorOf(Props(new ManagerMessageListener(emnotificationWriterActor)), "ManagerMessageListener")

    // Create the actor for project: event-management
    val eventManagementListener: ActorRef = system.actorOf(Props(new EventManagementListener(
      cateringMessageListener, entertainmentMessageListener, decorationsMessageListener, logisticsMessageListener, managerMessageListener
    )), "EventManagementListener")

    val consumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers("kafka1"+":9092")
      .withGroupId("group1")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    // Create and start the consumers (i.e, messageListeners)
    def listeners(topic: String, listener: ActorRef): Unit = {
      Consumer
        .plainSource(consumerSettings, Subscriptions.topics(topic))
        .map{ record => record.value().parseJson.convertTo[KafkaMessageFormat] }
        .runWith(
          Sink.actorRef[KafkaMessageFormat](
            ref = listener,
            onCompleteMessage = "complete",
            onFailureMessage = (throwable: Throwable) => s"Exception encountered"
          )
        )
    }

    // Configure listeners
    listeners(MessageTopics.EVENT_MANAGEMENT_TOPIC, eventManagementListener)
  }
}