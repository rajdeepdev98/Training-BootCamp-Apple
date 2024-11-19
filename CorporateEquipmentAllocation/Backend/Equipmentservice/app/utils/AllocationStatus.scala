package utils

import play.api.libs.json.Json
import slick.jdbc.JdbcType
import slick.jdbc.MySQLProfile.api._

object AllocationStatus  extends Enumeration{
  type AllocationStatus = Value

  val ACTIVE,INACTIVE=Value

  implicit val AllocationStatusFormat = Json.formatEnum(this)

  implicit val allocationStatusMapper: JdbcType[AllocationStatus] with BaseColumnType[AllocationStatus] =
    MappedColumnType.base[AllocationStatus, String](
      e => e.toString,
      s => AllocationStatus.withName(s)
    )
}
