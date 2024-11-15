package models.entity

import play.api.libs.json._

case class Issue(id: Option[Long] = None, taskId: Long, eventId: Long, teamId: Long, issueType: String,
                 issueDescription: String, reportedAt: String, resolvedAt: Option[String] = None)

object Issue {
  implicit val issueFormat: OFormat[Issue] = Json.format[Issue]
}
