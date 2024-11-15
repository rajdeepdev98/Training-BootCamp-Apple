package utils

import play.api.libs.json.Json



object EquipmentStatus extends Enumeration{
  type EquipmentStatus = Value

  val AVAILABLE, MAINTENANCE, ISSUED , RETURNED=Value

  implicit val EquipmentStatsFormat = Json.formatEnum(this)

}
