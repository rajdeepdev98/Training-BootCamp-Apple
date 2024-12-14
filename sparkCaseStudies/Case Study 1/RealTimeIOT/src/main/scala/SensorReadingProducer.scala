package org.dataeng.cs1

import akka.actor.{Actor, ActorSystem, Props}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.dataeng.cs1.model.SensorReading
import spray.json.{DefaultJsonProtocol, enrichAny}

import scala.concurrent.duration._
import scala.util.Random

object SensorReadingJsonProtocol extends DefaultJsonProtocol {
  implicit val sensorReadingFormat = jsonFormat4(SensorReading)
}

class SensorReadingProducer extends Actor {
  import SensorReadingJsonProtocol._
  import context.dispatcher

  implicit val materializer: Materializer =Materializer(context.system)
  val producerSettings = ProducerSettings(context.system, new StringSerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")

  val random = new Random()

//  context.system.scheduler.scheduleWithFixedDelay(0.milliseconds, 100.milliseconds, self, "generate")

  def receive: Receive = {
    case "generate" =>
      val reading = SensorReading(
        sensorId = java.util.UUID.randomUUID().toString,
        timestamp = System.currentTimeMillis(),
        temperature = random.nextFloat() * 200 - 50,
        humidity = random.nextFloat() * 100
      )
      val json = reading.toJson.compactPrint
      val record = new ProducerRecord[String, String]("sensor-readings", reading.sensorId, json)
      Source.single(record).runWith(Producer.plainSink(producerSettings))
      println("Sensor reading sent...")
  }
}

//object SensorReadingProducerApp extends App {
//  val system = ActorSystem("SensorReadingProducerSystem")
//  val producer = system.actorOf(Props[SensorReadingProducer], "SensorReadingProducer")
//}