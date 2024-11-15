package models.entity

import play.api.libs.json._

case class Team( id: Option[Long] = None, teamName: String, teamType: String)

object Team {
  implicit val teamFormat: OFormat[Team] = Json.format[Team]
}
