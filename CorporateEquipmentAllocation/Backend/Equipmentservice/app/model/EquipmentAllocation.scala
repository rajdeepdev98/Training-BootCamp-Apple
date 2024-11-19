package model
import play.api.libs.json.OFormat
import slick.jdbc.H2Profile.MappedJdbcType
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag
import utils.AllocationStatus
import utils.AllocationStatus.AllocationStatus

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class EquipmentAllocation(
                                id: Long,
                                employeeId: Long,
                                employeeName:String,
                                employeeEmail:String,
                                allocatedDate:LocalDateTime,
                                expectedReturnDate: LocalDateTime,
                                returnDate: LocalDateTime,
                                reason:String,
                                equipmentId:Long,
                                status:AllocationStatus

)

object EquipmentAllocation{

  import play.api.libs.json.Json
  implicit val equipmentAllocationFormat :OFormat[EquipmentAllocation]= Json.format[EquipmentAllocation]
//  implicit def StringToLocalDateTime(str: String): LocalDateTime = {
//      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//      LocalDateTime.parse(str, formatter)
//  }

//  def unapply(e: EquipmentAllocation): Option[(Long, Long, String, String, LocalDateTime, LocalDateTime, LocalDateTime, String, Long, String)] =
//    Some((e.id, e.employeeId, e.employeeName, e.employeeEmail, e.allocatedDate, e.expectedReturnDate, e.returnDate, e.reason, e.equipmentId, e.status.toString))

  implicit def TupleToEquipmentAllocation(t: (Long, Long,String,String,LocalDateTime, LocalDateTime,LocalDateTime,String,Long,String)): EquipmentAllocation = EquipmentAllocation(t._1, t._2, t._3, t._4,t._5,t._6,t._7,t._8,t._9,AllocationStatus.withName(t._10))
}


 class EquipmentAllocationTable(tag:Tag) extends Table[EquipmentAllocation](tag,"equipment_allocation"){
   private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
   implicit val localDateTimeMapper: BaseColumnType[LocalDateTime] = MappedJdbcType.base[LocalDateTime, String](
     ldt => ldt.format(formatter),
     s => LocalDateTime.parse(s, formatter)
   )
   implicit val AllocationStatusToStringMapper = MappedColumnType.base[AllocationStatus, String](
     e => e.toString,
     s => AllocationStatus.withName(s)
   )
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def employeeId = column[Long]("employee_id")
  def employeeName=column[String]("employee_name")
  def employeeEmail=column[String]("employee_email")
  def allocatedDate=column[LocalDateTime]("allocated_date")(localDateTimeMapper)
  def expectedReturnDate = column[LocalDateTime]("expected_return_date")(localDateTimeMapper)
  def returnDate = column[LocalDateTime]("return_date")(localDateTimeMapper)
  def reason = column[String]("reason")
  def equipmentId= column[Long]("equipment_id")
  def status=column[AllocationStatus]("status")
  def equipmentFk=foreignKey("equipment_fk",equipmentId,TableQuery[EquipmentTable])(_.id)
  def * = (id, employeeId,employeeName,employeeEmail, allocatedDate,expectedReturnDate, returnDate, reason,equipmentId,status) <> ((EquipmentAllocation.apply _).tupled, EquipmentAllocation.unapply )
}








