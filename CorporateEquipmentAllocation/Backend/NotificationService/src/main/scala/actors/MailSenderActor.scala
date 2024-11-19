package org.trainingapp.notificationservice
package actors

import akka.actor.Actor
import org.trainingapp.notificationservice.schemas.NotificationSchemas.SimpleMailSchema
import org.trainingapp.notificationservice.service.MailService
import spray.json.JsonParser

import scala.concurrent.ExecutionContext

class MailSenderActor extends Actor {
  implicit val ec: ExecutionContext = context.system.dispatcher
  override def receive: Receive = {
    case messageJson: String => {

      println(s"Received message: $messageJson")

      try {

        val mail: SimpleMailSchema = JsonParser.apply(messageJson).convertTo[SimpleMailSchema]
        MailService.sendMail(mail)
      } catch {
        case e: Exception => {
          println(s"Error parsing message: ${e.getMessage}")
        }
      }
    }
    case _ => println("Received unknown message")

  }
}
