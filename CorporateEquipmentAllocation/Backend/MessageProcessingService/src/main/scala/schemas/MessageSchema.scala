package com.training.app.schemas

import spray.json.{JsonParser, _}

import java.time.LocalDateTime


case class MessageSchema(
                          messageType:String,
                          employeeId: Long,
                          employeeName:String,
                          employeeEmail:String,
                          allocatedDate:LocalDateTime,
                          expectedReturnDate: LocalDateTime,
                          returnDate: LocalDateTime,
                          reason:String,
                          equipmentId:Long,
                          //                         status:AllocationStatus
                          deviceId:String,
                          name: String,
                          description: String,
                          category: String,
                          image: String,
                          status:String
                        )
object MessageSchema extends DefaultJsonProtocol{

  implicit object LocalDateTimeFormat extends RootJsonFormat[LocalDateTime] {
    override def write(obj: LocalDateTime): JsValue = JsString(obj.toString)

    override def read(json: JsValue): LocalDateTime = json match {
      case JsString(s) => LocalDateTime.parse(s)
      case _ => throw DeserializationException("Expected a string for LocalDatetime")
    }
  }
  implicit val messageSchemaFormat :RootJsonFormat[MessageSchema] = jsonFormat15(MessageSchema.apply)

  def stringToMessageSchema(str: String): MessageSchema = {
    try {
      val jsonAst = JsonParser(str)
      jsonAst.convertTo[MessageSchema]
    } catch {
      case e: Exception => throw DeserializationException(s"Error deserializing string to MessageSchema: ${e.getMessage}")
    }
  }


}


