package services

import models.entity.{Event, Task}
import repositories.EventRepository

import java.time.LocalDate
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EventService @Inject() (eventRepository: EventRepository, taskService: TaskService)
                             (implicit ex: ExecutionContext) {
  def create(event: Event): Future[Event] = {
    eventRepository.create(event)
  }

  def getEventById(eventId: Long): Future[Event] = eventRepository.getEventById(eventId)

  def update(eventId: Long, event: Event): Future[Event] = eventRepository.update(eventId, event)

  def updateEventStatus(eventId: Long, newStatus: String): Future[Event] = eventRepository.updateEventStatus(eventId, newStatus)

  def list(eventType: Option[String], status: Option[String], eventDate: Option[LocalDate], slotNumber: Option[Int])
  : Future[Seq[Event]] = eventRepository.listEvents(eventType: Option[String], status: Option[String], eventDate: Option[LocalDate], slotNumber: Option[Int])

  def getTasksForEventId(eventId: Long): Future[Seq[Task]] = taskService.getTasksForEventId(eventId)
}
