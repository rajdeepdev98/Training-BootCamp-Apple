package com.training.app

import akka.actor.Actor
class NotificationConsumerActor (topic:String) extends Actor{

  override def receive: Receive = {

    case _=>println("Received a message")

  }
}
