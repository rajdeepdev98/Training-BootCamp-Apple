package services

import models.AssignTasksRequest
import models.entity.Task
import repositories.TaskRepository

import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaskService @Inject() (taskRepository: TaskRepository, kafkaProducerFactory: KafkaProducerFactory)
                            (implicit executionContext: ExecutionContext) {
  def create(task: Task): Future[Task] = taskRepository.create(task)

  def getEventById(taskId: Long): Future[Task] = taskRepository.getEventById(taskId)

  def updateStatus(taskId: Long, status: String): Future[Task] = {
    taskRepository.updateStatus(taskId, status)
  }

  def getTasksForEventId(eventId: Long): Future[Seq[Task]] = taskRepository.getTasksForEventId(eventId)

  def assignTasks(req: AssignTasksRequest): Future[List[Task]] = {
    val tasksList = req.tasks.foldLeft(List.empty[Task])((acc, ele) => {
      val task = Task(
        eventId =  req.eventId,
        teamId = ele.teamId,
        taskDescription = ele.taskDescription,
        deadLine = ele.deadLine,
        specialInstructions = ele.specialInstructions,
        status = "ASSIGNED",
        createdAt = LocalDateTime.now().toString
      )
      acc :+ task
    })
    taskRepository.assignTasks(tasksList).map { lists =>
      kafkaProducerFactory.sendTasksAssignmentList(lists)
      lists
    }
  }
}
