package model

import play.api.libs.json.{Json, OFormat}
import utils.AllocationStatus.AllocationStatus
import utils.EquipmentStatus.EquipmentStatus

import java.time.LocalDateTime

case class MessageSchema(
                          messageType:String,
                          employeeId: Long,
                          employeeName:String,
                          employeeEmail:String,
                          allocatedDate:LocalDateTime,
                          expectedReturnDate: LocalDateTime,
                          returnDate: LocalDateTime,
                          reason:String,
                          equipmentId:Long,
//                         status:AllocationStatus
                          deviceId:String,
                          name: String,
                          description: String,
                          category: String,
                          image: String,
                          status:EquipmentStatus
                        )

object MessageSchema{

  implicit val messageSchemaFormat :OFormat[MessageSchema] = Json.format[MessageSchema]
}


