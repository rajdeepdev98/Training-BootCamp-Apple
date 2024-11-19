package com.training.app
package utils

import schemas.MessageSchema
import utils.Constants._
import Conversions._

object MessageProcessor {



  def processedMessage(message:MessageSchema):List[String]={

    message.messageType match{

      case REMINDER=>{
        List(MessageSchemaToReminderMessage(message))
      }
      case ALLOCATION=>{
        List(MessageSchemaToEmployeeAllocationAlertMessage(message),MessageSchemaToInventoryAlertMessage(message))
      }

      case RETURN=>{

        val list= List(MessageSchemaToEquipmentReturnEmployeeAcknowledgementMessage(message),MessageSchemaToInventoryReturnMessage(message))
        if(message.status.equals("MAINTENANCE"))list:+MessageSchemaToMaintenanceAlertMessage(message)

        list

    }



    }
  }





}
