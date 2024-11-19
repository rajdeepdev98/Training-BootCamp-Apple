package com.training.app
package schemas

import utils.Constants.{FIXED, RECURRING}

import spray.json.{DefaultJsonProtocol, RootJsonFormat}


object MailSchemas extends DefaultJsonProtocol {

  case class ReturnReminderSchema(subject:String,toEmail:String,body:String,messageType:String=RECURRING)

  case class MaintenanceAlertSchema(subject:String,toEmail:String,body:String,messageType:String=FIXED)

  case class AllocationReminderSchema(subject:String,toEmail:String,body:String,messageType:String=RECURRING)

  case class SimpleMailSchema(subject:String,toEmail:String,body:String)
  implicit val simpleMailSchemaFormat: RootJsonFormat[SimpleMailSchema] = jsonFormat3(SimpleMailSchema)



}
