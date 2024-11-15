package models

import play.api.libs.json._

case class KafkaMessageFormat(receiver: String, messageType: String, message: String)

object KafkaMessageFormat {
  implicit val teamFormat: OFormat[KafkaMessageFormat] = Json.format[KafkaMessageFormat]
}
