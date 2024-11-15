package repositories

import models.entity.Issue
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class IssueRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class IssueTable(tag: Tag) extends Table[Issue](tag, "issue") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def taskId = column[Long]("taskId")
    def eventId = column[Long]("eventId")
    def teamId = column[Long]("teamId")
    def issueType = column[String]("issueType")
    def issueDescription = column[String]("issueDescription")
    def reportedAt = column[String]("reportedAt")
    def resolvedAt = column[Option[String]]("resolvedAt")

    def * = (id.?, taskId, eventId, teamId, issueType, issueDescription, reportedAt, resolvedAt) <> ((Issue.apply _).tupled, Issue.unapply)
  }

  private val issues = TableQuery[IssueTable]

  def create(issue: Issue): Future[Issue] = {
    val insertQueryThenReturnId = issues returning issues.map(_.id)

    db.run(insertQueryThenReturnId += issue).flatMap(
      id => get(id))
  }

  def get(issueId: Long): Future[Issue] = db.run(issues.filter(_.id === issueId).result.head)
}
