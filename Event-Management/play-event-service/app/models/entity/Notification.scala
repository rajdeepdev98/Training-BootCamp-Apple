package models.entity

import play.api.libs.json._

case class Notification( id: Option[Long] = None, taskId: Long, teamId: Long, notificationType: String, sentAt: String)

object Notification {
  implicit val notificationFormat: OFormat[Notification] = Json.format[Notification]
}
