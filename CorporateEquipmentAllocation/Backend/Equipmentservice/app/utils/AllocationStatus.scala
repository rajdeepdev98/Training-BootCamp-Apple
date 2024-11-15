package utils

import play.api.libs.json.Json

object AllocationStatus  extends Enumeration{
  type AllocationStatus = Value

  val ACTIVE,INACTIVE=Value

  implicit val AllocationStatusFormat = Json.formatEnum(this)
}
