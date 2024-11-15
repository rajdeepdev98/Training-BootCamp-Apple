package models.entity

import play.api.libs.json._

case class Task( id: Option[Long] = None, eventId: Long, teamId: Long, taskDescription: String, deadLine: String,
                 specialInstructions: Option[String], status: String, createdAt: String)

object Task {
  implicit val taskFormat: OFormat[Task] = Json.format[Task]
}
