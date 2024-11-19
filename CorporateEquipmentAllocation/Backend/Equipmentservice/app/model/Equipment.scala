package model

import play.api.libs.json.OFormat
import slick.jdbc.MySQLProfile.api._
import slick.lifted.Tag
import utils.EquipmentStatus
import utils.EquipmentStatus.EquipmentStatus

case class Equipment(
  id: Long,
  deviceId:String,
  name: String,
  description: String,
  category: String,
  image: String,
  status:EquipmentStatus
)
object Equipment {
  import play.api.libs.json.Json
  implicit val equipmentFormat :OFormat[Equipment] = Json.format[Equipment]
  implicit def TupleToEquipment(t: (Long, String, String, String, String,String,String)): Equipment = Equipment(t._1, t._2, t._3, t._4, t._5,t._6,EquipmentStatus.withName(t._7))
  //what does the above line do?  It creates a JSON formatter for the Equipment case class
  //what does the below line do? It converts a tuple to an Equipment case class


}

class EquipmentTable(tag: Tag) extends Table[Equipment](tag,"equipments"){
  implicit val EquipmentStatusToStringMapper = MappedColumnType.base[EquipmentStatus, String](
    e => e.toString,
    s => EquipmentStatus.withName(s)
  )
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def deviceId= column[String]("deviceId",O.Unique)
  def name = column[String]("name")
  def description = column[String]("description")
  def category = column[String]("category")
  def image = column[String]("image")
  def status = column[EquipmentStatus]("status")
  def * = (id, deviceId, name, description, category, image,status) <> ((Equipment.apply _).tupled, Equipment.unapply)

//  def * = (id, deviceId, name, description, category, image,status) <> ((Equipment.apply _).tupled, Equipment.unapply)
}
