package utils

import play.api.libs.json.Json
import slick.jdbc.JdbcType
import slick.jdbc.MySQLProfile.api._

object EquipmentStatus extends Enumeration{
  type EquipmentStatus = Value

  val AVAILABLE, MAINTENANCE, ALLOCATED =Value

  implicit val EquipmentStatsFormat = Json.formatEnum(this)

  implicit val equipmentStatusMapper: JdbcType[EquipmentStatus] with BaseColumnType[EquipmentStatus] =
    MappedColumnType.base[EquipmentStatus, String](
      e => e.toString,
      s => EquipmentStatus.withName(s)
    )

}
