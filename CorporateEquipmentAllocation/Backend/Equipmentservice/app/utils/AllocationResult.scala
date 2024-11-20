package utils

import model.EquipmentAllocation
import utils.AllocationStatus.AllocationStatus

import java.time.LocalDateTime

case class AllocationResult(id: Long,
                            employeeId: Long,
                            employeeName:String,
                            employeeEmail:String,
                            allocatedDate:LocalDateTime,
                            expectedReturnDate: LocalDateTime,
                            returnDate: Option[LocalDateTime],
                            reason:String,
                            equipmentId:Long,
                            status:AllocationStatus)

object AllocationResult {

   import play.api.libs.json.Json

    implicit val format=Json.format[AllocationResult]

    implicit def EquipmentAllocationToAllocationResult(e: EquipmentAllocation): AllocationResult = {
      var ret:Option[LocalDateTime]=Some(e.returnDate)
      if(e.status==AllocationStatus.ACTIVE)ret=None
      AllocationResult(e.id, e.employeeId, e.employeeName, e.employeeEmail, e.allocatedDate, e.expectedReturnDate,ret, e.reason, e.equipmentId, e.status)
    }
}
