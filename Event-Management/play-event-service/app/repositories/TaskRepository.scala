package repositories

import models.entity.Task
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaskRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class TaskTable(tag: Tag) extends Table[Task](tag, "task")  {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def eventId = column[Long]("eventId")
    def teamId = column[Long]("teamId")
    def taskDescription = column[String]("taskDescription")
    def deadLine = column[String]("deadline")
    def specialInstructions = column[Option[String]]("specialInstructions")
    def status = column[String]("status")
    def createdAt = column[String]("createdAt")

    def * = (id.?, eventId, teamId, taskDescription, deadLine, specialInstructions, status, createdAt) <> ((Task.apply _).tupled, Task.unapply)
  }

  private val tasks = TableQuery[TaskTable]

  def create(task: Task): Future[Task] = {
    val insertQuery = tasks returning tasks.map(_.id) into ((eventData, id) => eventData.copy(id = Some(id)))

    db.run(insertQuery += task)
  }

  def getEventById(taskId: Long): Future[Task] = {
    db.run(tasks.filter(_.id === taskId).result.head)
  }

  def updateStatus(taskId: Long, status: String): Future[Task] = {
    val updateQuery = tasks.filter(_.id === taskId)
      .map(ele => ele.status)
      .update(status)

    db.run(updateQuery).flatMap { _ =>
      getEventById(taskId)
    }
  }

  def getTasksForEventId(eventId: Long): Future[Seq[Task]] = {
    val query = tasks.filter(_.eventId === eventId).result
    db.run(query)
  }

  def assignTasks(tasksList: List[Task]): Future[List[Task]] = {
    val actions = tasksList.map { task =>
      (tasks returning tasks.map(_.id) into ((task, id) => task.copy(id = Some(id)))) += task
    }

    db.run(DBIO.sequence(actions)).map(_.toList)
  }

}