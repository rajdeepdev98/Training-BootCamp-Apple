package repository


import model.{Equipment, EquipmentTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.lifted

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class EquipmentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._






  private val equipments = lifted.TableQuery[EquipmentTable]

  def list(): Future[Seq[Equipment]] = db.run(equipments.result)

  def getById(id:Long): Future[Option[Equipment]] = {
    db.run(equipments.filter(_.id ===id).result.headOption)
  }

  def add(equipment: Equipment): Future[Equipment] = {
//    val equipment = Equipment(0, "name", "description", "category", "image")
    db.run(equipments returning equipments.map(_.id) += equipment).map(id => equipment.copy(id = id))
  }

  def update(equipment: Equipment): Future[Int] = {
    db.run(equipments.filter(_.id === equipment.id).update(equipment))
  }

  def delete(id: Long): Future[Int] = {
    db.run(equipments.filter(_.id === id).delete)
  }


}







