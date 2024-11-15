package models

import play.api.libs.json._

case class ListEventsRequest(eventType: Option[String] = None, status: Option[String] = None,
                             eventDate: Option[String] = None, slotNumber: Option[Int] = None)

object ListEventsRequest {
  implicit val teamFormat: OFormat[ListEventsRequest] = Json.format[ListEventsRequest]
}
