package org.trainingapp.notificationservice
package service

import com.typesafe.config.ConfigFactory
import schemas.NotificationSchemas.SimpleMailSchema

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Authenticator, Message, MessagingException, PasswordAuthentication, Session, Transport}
import scala.concurrent.{ExecutionContext, Future}
object MailService {
  implicit val ec:ExecutionContext = ExecutionContext.global
  private val config = ConfigFactory.load()
  // Create a session with the given mail server settings
  val smtpUser: String = config.getString("mail.smtp.user")
  val smtpPassword: String = config.getString("mail.smtp.password")
  private val session: Session = {
    val props = new Properties()
    props.put("mail.smtp.host", config.getString("mail.smtp.host"))
    props.put("mail.smtp.port", config.getInt("mail.smtp.port"))
    props.put("mail.smtp.auth", config.getBoolean("mail.smtp.auth"))
    props.put("mail.smtp.starttls.enable", config.getBoolean("mail.smtp.starttls.enable"))  // Enable STARTTLS for security
    props.put("mail.smtp.ssl.trust", config.getString("mail.smtp.ssl.trust"))
    props.put("mail.debug", true)
    props.put("mail.smtp.ssl.enable", true)
    // Trust the host for SSL


    Session.getInstance(props, new Authenticator {
      override protected def getPasswordAuthentication: PasswordAuthentication = {
        new PasswordAuthentication(smtpUser, smtpPassword)
      }
    })
  }
    def sendMail(mail:SimpleMailSchema):  Future[Unit] = {
//      println(s"Sending mail to ${mail.toEmail} with subject ${mail.subject} and body ${mail.body}")
      //making the call asynchronous
      Future {
        sendEmail(mail.toEmail, mail.subject, mail.body)
      }
    }
  def sendEmail(to: String, subject: String, body: String): Unit = {
    try {
      val message = new MimeMessage(session)
      message.setFrom(new InternetAddress(smtpUser))
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to))
      message.setSubject(subject)
      message.setText(body)
      println("Sending email...")
      // Send the message
      Transport.send(message)
      println(s"Email sent successfully to $to!")
    } catch {
      case e: MessagingException =>
        println(s"Failed to send email: ${e.getMessage}")
        e.printStackTrace()
    }
  }

}
