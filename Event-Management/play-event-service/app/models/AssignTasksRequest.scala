package models

import play.api.libs.json._

case class TasksRequest(teamId: Long, taskDescription: String,
                        deadLine: String, specialInstructions: Option[String])

object TasksRequest {
  implicit val taskFormat: OFormat[TasksRequest] = Json.format[TasksRequest]
}

case class AssignTasksRequest(
                              eventId: Long,
                              tasks: Seq[TasksRequest]
                            )

object AssignTasksRequest {
  implicit val taskFormat: OFormat[AssignTasksRequest] = Json.format[AssignTasksRequest]
}
