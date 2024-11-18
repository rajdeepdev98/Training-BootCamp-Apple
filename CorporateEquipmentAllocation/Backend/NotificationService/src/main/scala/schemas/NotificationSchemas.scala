package org.trainingapp.notificationservice
package schemas

import spray.json.DefaultJsonProtocol.jsonFormat3
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object NotificationSchemas extends DefaultJsonProtocol{

  case class SimpleMailSchema(subject:String,toEmail:String,body:String)
  case class PushNotificationSchema(title:String, message:String, token:String)
  implicit val simpleMailSchemaFormat: RootJsonFormat[SimpleMailSchema] = jsonFormat3(SimpleMailSchema)



}
