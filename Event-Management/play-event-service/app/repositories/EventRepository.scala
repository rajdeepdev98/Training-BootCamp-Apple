package repositories

import models.entity.Event
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.time.LocalDate
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EventRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class EventTable(tag: Tag) extends Table[Event](tag, "event") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def eventType = column[String]("eventType")
    def eventName = column[String]("eventName")
    def eventDate = column[LocalDate]("eventDate")
    def slotNumber = column[Int]("slotNumber")
    def guestCount = column[Long]("guestCount")
    def specialRequirements = column[Option[String]]("specialRequirements")
    def eventStatus = column[Option[String]]("eventStatus")

    def * = (id.?, eventType, eventName, eventDate, slotNumber, guestCount, specialRequirements, eventStatus) <> ((Event.apply _).tupled, Event.unapply)
  }

  private val events = TableQuery[EventTable]

  def create(event: Event): Future[Event] = {
    val insertQuery = events returning events.map(_.id) into ((eventData, id) => eventData.copy(id = Some(id)))

    db.run(insertQuery += event)
  }

  def getEventById(eventId: Long): Future[Event] = {
    db.run(events.filter(_.id === eventId).result.head)
  }

  def update(eventId: Long, event: Event): Future[Event] = {
    val updateQuery = events.filter(_.id === eventId)
      .map(e => (e.eventType, e.eventName, e.eventDate, e.slotNumber, e.guestCount, e.specialRequirements, e.eventStatus))
      .update((event.eventType, event.eventName, event.eventDate, event.slotNumber, event.guestCount, event.specialRequirements, event.eventStatus))

    db.run(updateQuery).flatMap { _ =>
      getEventById(eventId)
    }
  }

  def updateEventStatus(eventId: Long, newStatus: String): Future[Event] = {
    val updateQuery = events.filter(_.id === eventId)
      .map(_.eventStatus)
      .update(Some(newStatus))

    db.run(updateQuery).flatMap {
      case 0 =>
        Future.failed(new Exception(s"Event with ID $eventId not found"))
      case _ =>
        getEventById(eventId)
    }
  }

  def listEvents(eventType: Option[String], status: Option[String], eventDate: Option[LocalDate], slotNumber: Option[Int]): Future[Seq[Event]] = {
    val query = events
      .filterOpt(status) { case (event, s) => event.eventStatus === s }
      .filterOpt(eventType) { case (event, s) => event.eventType === s }
      .filterOpt(eventDate) { case (event, s) => event.eventDate === s }
      .filterOpt(slotNumber) { case (event, s) => event.slotNumber === s }

    db.run(query.result)
  }

  def getEventsByDate(date: LocalDate): Future[Seq[Event]] = {
    db.run(events.filter(event => event.eventDate === date).result)
  }
}
