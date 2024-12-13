package org.dataeng.cs1

import akka.actor.{ActorSystem, Props}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

object MainAppApp extends App  {
  implicit val ec: ExecutionContext = ExecutionContext.global
  val system = ActorSystem("SensorReadingProducerSystem")
  val producer = system.actorOf(Props[SensorReadingProducer], "SensorReadingProducer")

  //generating sensor readings every 100 milliseconds

  system.scheduler.scheduleWithFixedDelay(0.milliseconds, 1.second, producer, "generate")

  println("starting...")




}
