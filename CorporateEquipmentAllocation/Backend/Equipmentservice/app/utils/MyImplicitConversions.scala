package utils

import model.{Equipment, EquipmentAllocation, MessageSchema}

object MyImplicitConversions {

  implicit def tupleEquipToMessageSchema(t: (EquipmentAllocation, Equipment,String)): MessageSchema = {

    val equipmentAllocation = t._1
    val equipment = t._2
    val messageType=t._3
    MessageSchema(
      messageType,
      equipmentAllocation.employeeId,
      equipmentAllocation.employeeName,
      equipmentAllocation.employeeEmail,
      equipmentAllocation.allocatedDate,
      equipmentAllocation.expectedReturnDate,
      equipmentAllocation.returnDate,
      equipmentAllocation.reason,
      equipment.id,
      equipment.deviceId,
      equipment.name,
      equipment.description,
      equipment.category,
      equipment.image,
      equipment.status
    )
  }
}
