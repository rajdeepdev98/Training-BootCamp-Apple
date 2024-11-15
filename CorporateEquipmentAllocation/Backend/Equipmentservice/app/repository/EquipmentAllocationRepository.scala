package repository

import model.{EquipmentAllocation, EquipmentAllocationTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.lifted

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EquipmentAllocationRepository @Inject() (dbConfigProvider:DatabaseConfigProvider)(implicit ec:ExecutionContext){

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

//  private class EquipmentAllocationTable(tag:Tag) extends Table[EquipmentAllocation](tag,"equipment_allocation"){
//    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
//    def employeeId = column[Long]("employee_id")
//    def startDate = column[LocalDateTime]("start_date")
//    def endDate = column[LocalDateTime]("end_date")
//    def reason = column[String]("reason")
//    def equipmentId= column[Long]("equipment_id")
////    def equipmentFk=foreignKey("equipment_fk",equipmentId,TableQuery[Equipment])(_.id)
//    def * = (id, employeeId, startDate, endDate, reason) <> ((EquipmentAllocation.apply _).tupled, EquipmentAllocation.unapply)
//  }


  private val equipmentAllocations =lifted.TableQuery[EquipmentAllocationTable]


  def list(): Future[Seq[EquipmentAllocation]] = db.run(equipmentAllocations.result)

  def getById(id: Long): Future[Option[EquipmentAllocation]] = {
    db.run(equipmentAllocations.filter(_.id === id).result.headOption)
  }

  def add(equipmentAllocation: EquipmentAllocation): Future[EquipmentAllocation] = {
    db.run(equipmentAllocations returning equipmentAllocations.map(_.id) += equipmentAllocation).map(id => equipmentAllocation.copy(id = id))
  }

  def update(equipmentAllocation: EquipmentAllocation): Future[Int] = {
    db.run(equipmentAllocations.filter(_.id === equipmentAllocation.id).update(equipmentAllocation))
  }

  def delete(id: Long): Future[Int] = {
    db.run(equipmentAllocations.filter(_.id === id).delete)
  }

  //returns Equipment
//  def returnEquipment(id: Long,status:String): Future[Option[EquipmentAllocation]] = {
//
//  }
//  lazy val equipment = TableQuery[EquipmentAllocationTable]
//

}

