package repositories

import models.entity.Team
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TeamRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class TeamsTable(tag: Tag) extends Table[Team](tag, "team")  {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def teamName = column[String]("teamName")
    def teamType = column[String]("teamType")

    def * = (id.?, teamName, teamType) <> ((Team.apply _).tupled, Team.unapply)
  }

  private val teams = TableQuery[TeamsTable]

  def create(team: Team): Future[Team] = {
    val insertQuery = teams returning teams.map(_.id) into ((eventData, id) => eventData.copy(id = Some(id)))

    db.run(insertQuery += team)
  }

  def getTeamDetailsById(teamId: Long): Future[Team] = {
    db.run(teams.filter(_.id === teamId).result.head)
  }

  def listTeams(teamType: Option[String]): Future[Seq[Team]] = {
    val query = teams.filterOpt(teamType) { case (team, s) => team.teamType === s }

    db.run(query.result)
  }

}
