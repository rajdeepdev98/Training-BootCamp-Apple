package models.entity

import play.api.libs.json._

import java.time.LocalDate

case class Event(id: Option[Long] = None, eventType: String, eventName: String, eventDate: LocalDate,
                 slotNumber: Int, guestCount: Long, specialRequirements: Option[String] = None,
                 eventStatus: Option[String] = None)

object Event {
  implicit val eventFormat: OFormat[Event] = Json.format[Event]
}
