package com.training.app.utils


object Status{

  object EquipmentStatus extends Enumeration {
    type EquipmentStatus = Value

    val AVAILABLE, MAINTENANCE, ALLOCATED = Value
  }

  object AllocationStatus  extends Enumeration {
    type AllocationStatus = Value

    val ACTIVE, INACTIVE = Value
  }


}
