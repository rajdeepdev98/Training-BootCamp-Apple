package com.training.app
package utils

import schemas.MailSchemas.{SimpleMailSchema, simpleMailSchemaFormat}
import schemas.MessageSchema
import utils.Constants.MAINTENANCE

import com.typesafe.config.ConfigFactory
import spray.json._



object Conversions {

  val config = ConfigFactory.load()

  implicit def MessageSchemaToReminderMessage(message: MessageSchema): String = {

    val toEmail = message.employeeEmail
    val subject = "Your allocated Equipment is Overdue"
    val body =
      s"""
         |Dear ${message.employeeName},
         |
         |This is a reminder to return the equipment you have borrowed.
         |Equipment Name: ${message.name}
         |Device Id: ${message.deviceId}
         |Expected Return Date: ${message.expectedReturnDate}
         |
         |Regards,
         |Equipment AllocationTeam
         |""".stripMargin
//    ReturnReminderSchema(subject, toEmail, body)
    SimpleMailSchema(subject, toEmail, body).toJson.toString()


  }
  implicit def MessageSchemaToEmployeeAllocationAlertMessage(messageSchema: MessageSchema): String = {
    val toEmail = messageSchema.employeeEmail
    val subject = "Your Equipment Has Been Allocated"
    val body =
      s"""
         |Dear ${messageSchema.employeeName},
         |
         |The equipment with the following details has been allocated to you:
         |Equipment Name: ${messageSchema.name}
         |Device Id: ${messageSchema.deviceId}
         |Allocated Date: ${messageSchema.allocatedDate}
         |
         |Regards,
         |Equipment Allocation Team
         |""".stripMargin

    SimpleMailSchema(subject, toEmail, body).toJson.toString()
  }

  implicit def MessageSchemaToMaintenanceAlertMessage(messageSchema: MessageSchema): String = {
    val toEmail = config.getString("mails.maintenance.email")
    val subject = "Maintenance Alert"
    val body =
      s"""
         |Dear Maintenance Team,
         |
         |The equipment with the following details has been returned and is sent for maintenance:
         |Employee Name: ${messageSchema.employeeName}
         |Device Id: ${messageSchema.deviceId}
         |Equipment Name: ${messageSchema.name}
         |Return Date: ${messageSchema.returnDate}
         |
         |Regards,
         |Equipment Allocation Team
         |""".stripMargin
//    MaintenanceAlertSchema(subject, toEmail, body)
       SimpleMailSchema(subject, toEmail, body).toJson.toString()
  }
  implicit def MessageSchemaToInventoryAlertMessage(messageSchema: MessageSchema): String= {
    val toEmail = config.getString("mails.inventory.email")
    val subject = "Equipment Allocation Alert"
    val body =
      s"""
         |Dear Inventory  Team,
         |
         |The equipment with the following details has been allocated:
         |Employee Name: ${messageSchema.employeeName}
         |Device Id: ${messageSchema.deviceId}
         |Equipment Name: ${messageSchema.name}
         |Allocated Date: ${messageSchema.allocatedDate}
         |
         |Regards,
         |Equipment Allocation Team
         |""".stripMargin

    SimpleMailSchema(subject, toEmail, body).toJson.toString()

  }

  implicit def MessageSchemaToInventoryReturnMessage(messageSchema: MessageSchema): String= {
    val toEmail = config.getString("mails.inventory.email")
    val subject = "Equipment Return Alert"
    val body =
      s"""
         |Dear Inventory Team,
         |
         |The equipment with the following details has been returned:
         |Employee Name: ${messageSchema.employeeName}
         |Device Id: ${messageSchema.deviceId}
         |Equipment Name: ${messageSchema.name}
         |Return Date: ${messageSchema.returnDate}
         |${if(messageSchema.status.equals(MAINTENANCE)) "and is sent for maintenance" else ""})}
         |
         |Regards,
         |Equipment Allocation Team
         |""".stripMargin

    SimpleMailSchema(subject, toEmail, body).toJson.toString()

  }
  implicit def MessageSchemaToEquipmentReturnEmployeeAcknowledgementMessage(messageSchema: MessageSchema): String= {
    val toEmail = messageSchema.employeeEmail
    val subject = "Equipment Return Acknowledgement"
    val body =
      s"""
         |Dear ${messageSchema.employeeName},
         |
         |The equipment with the following details has been returned:
         |Device Id: ${messageSchema.deviceId}
         |Equipment Name: ${messageSchema.name}
         |Return Date: ${messageSchema.returnDate}
         |${if(messageSchema.status.equals(MAINTENANCE)) "and is sent for maintenance" else ""}
         |
         |Regards,
         |Equipment Allocation Team
         |""".stripMargin

    SimpleMailSchema(subject, toEmail, body).toJson.toString()

  }

}

