package com.training.app

import schemas.MessageSchema

import akka.actor.ActorSystem
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ConsumerSettings, ProducerSettings, Subscriptions}
import akka.stream.scaladsl.{Flow, Keep, Source}
import akka.{Done, NotUsed}
import com.training.app.utils.MessageProcessor
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import spray.json.JsonParser

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("MessageProcessingService")
  implicit val ec = system.dispatcher

  val config = ConfigFactory.load()
  val consumerConfig = config.getConfig("akka.kafka.consumer")
  val producerConfig = config.getConfig("akka.kafka.producer")

  val consumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers(consumerConfig.getString("bootstrap.servers"))
    .withGroupId(consumerConfig.getString("group.id"))
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerConfig.getString("auto.offset.reset"))

  val producerSettings = ProducerSettings(system, new StringSerializer, new StringSerializer)
    .withBootstrapServers(producerConfig.getString("bootstrap.servers"))

  val consumerSource = Consumer.plainSource(consumerSettings, Subscriptions.topics(consumerConfig.getString("topic")))
  val producerSink = Producer.plainSink(producerSettings)

  def stringToMessageSchema(str: String): MessageSchema = {
    val jsonAst = JsonParser(str)
    jsonAst.convertTo[MessageSchema]
  }


  val processingFlow: Flow[String, ProducerRecord[String, String], NotUsed] = Flow[String]
    .map { value =>
      try {
        val messageSchema = stringToMessageSchema(value)
        val list: List[String] = MessageProcessor.processedMessage(messageSchema)
        val records: List[ProducerRecord[String, String]] = list.map(v => new ProducerRecord(producerConfig.getString("topic"), v))
        records

        //        List(new ProducerRecord(producerConfig.getString("topic"), "additional1"), new ProducerRecord(producerConfig.getString("topic"), "additional2")
        //        Source(v)
      } catch {
        case e: Exception =>
          println(s"Error processing message: $value")
          List.empty
      }
    }.mapConcat((x: Any) => x.asInstanceOf[IterableOnce[ProducerRecord[String, String]]])


      println("Starting the stream")

      val done: Future[Done] = consumerSource
        .map(_.value())
        .via(processingFlow)
        .runWith(producerSink)

      done.onComplete {
        case scala.util.Success(_) => println("Stream completed successfully")
        case scala.util.Failure(e) => println(s"Stream failed with error: $e")
      }

      println("Stream started... Consuming from input topic and producing to output topic")

      sys.addShutdownHook {
        println("Shutting down the stream")
        system.terminate()
      }


}
