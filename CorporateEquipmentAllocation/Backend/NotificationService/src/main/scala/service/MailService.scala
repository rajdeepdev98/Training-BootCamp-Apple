package org.trainingapp.notificationservice
package service

import schemas.NotificationSchemas.SimpleMailSchema

object MailService {


    def sendMail(mail:SimpleMailSchema): Unit = {
      println(s"Sending mail to ${mail.toEmail} with subject ${mail.subject} and body ${mail.body}")
    }

}
