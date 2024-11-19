package org.trainingapp.notificationservice

import akka.actor.{ActorSystem, Props}
import akka.kafka.{ConsumerSettings, Subscriptions}
import com.typesafe.config.ConfigFactory
import akka.kafka.scaladsl.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import actors.MailSenderActor

import akka.stream.scaladsl.Sink
object Main extends App {
  println("Hello, NotificationService!")

  implicit val system: ActorSystem = ActorSystem("NotificationServiceSystem")
  implicit val ec = system.dispatcher

  val config = ConfigFactory.load()
  val consumerConfig = config.getConfig("akka.kafka.consumer")


  val consumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers(consumerConfig.getString("bootstrapServers"))
    .withGroupId(consumerConfig.getString("groupId"))
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerConfig.getString("autoOffsetReset"))
    .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerConfig.getString("autoCommitEnable"))

  val consumerSource = Consumer.plainSource(consumerSettings, Subscriptions.topics(consumerConfig.getString("topic")))

  val mailSenderActor = system.actorOf(Props(new MailSenderActor), "mailSenderActor")

  consumerSource.map(record => record.value()).runWith(Sink.foreach(mailSenderActor ! _))


  //Adding a shutdown hook to terminate the actor system
  sys.addShutdownHook{
    println("Shutting down NotificationServiceSystem")
    system.terminate()
  }



}
