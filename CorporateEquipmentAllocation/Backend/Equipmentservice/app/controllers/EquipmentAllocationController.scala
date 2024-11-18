package controllers

import model.{EquipmentAllocation, MessageSchema}
import play.api.mvc.ControllerComponents
import repository.{EquipmentAllocationRepository, EquipmentRepository}
import play.api.libs.json._
import play.api.mvc._
import service.KafkaProducerService
import utils.{AllocationStatus, EquipmentStatus}
import utils.EquipmentStatus.EquipmentStatus
import utils.MyImplicitConversions._
import utils.Constants._

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EquipmentAllocationController @Inject()(cc:ControllerComponents, equipmentRepository:EquipmentRepository,equipmentAllocationRepository: EquipmentAllocationRepository,kafkaProducerService: KafkaProducerService)(implicit ec:ExecutionContext) extends AbstractController(cc) {


  implicit val jsonFormat = Json.format[EquipmentAllocation]

  def list() = Action.async {
    equipmentAllocationRepository.list().map(equipmentAllocations => Ok(Json.toJson(equipmentAllocations)))
  }
  def getById(id: Long) = Action.async {
    equipmentAllocationRepository.getById(id).map {
      case Some(equipmentAllocation) => Ok(Json.toJson(equipmentAllocation))
      case None => NotFound(Json.obj("message" -> "Equipment Allocation not found"))
    }
  }
  def add = Action.async(parse.json) { request =>
    request.body.validate[EquipmentAllocation].fold(
      errors => {
        println(errors)
        println("error")
        Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))
      },
      equipmentAllocation => {
        println(equipmentAllocation)
        try {
          equipmentAllocationRepository.allocate(equipmentAllocation).map{case (equipmentAllocation, equipment) => {
            {

              val kafkaMessage:MessageSchema=(equipmentAllocation,equipment,ALLOCATION)
              println(equipmentAllocation)
              kafkaProducerService.sendMessage("key",Json.toJson(kafkaMessage).toString())
              Created(Json.toJson(equipmentAllocation))
            }
          }
          }.recover{
            //handling the exceptions occuring within the Future returned
            case e: Exception => BadRequest(Json.obj("message" -> e.getMessage))
          }

        }catch {
          case e: Exception => Future.successful(BadRequest(Json.obj("message" -> e.getMessage))
          )
        }

      }
    )
//    equipmentAllocationRepository.add(equipmentAllocation).map(equipmentAllocation => Created(Json.toJson(equipmentAllocation)))
  }

//  def returnEquipment(id: Long) = Action.async {
//    equipmentAllocationRepository.returnEquipment(id).map {
//      case Some(equipmentAllocation) => Ok(Json.toJson(equipmentAllocation))
//      case None => NotFound(Json.obj("message" -> "Equipment Allocation not found"))
//    }
//  }
  //defining the return logic
def returnEquipment(id:Long,status:String)=Action.async{
  println(status)
  try {
    equipmentAllocationRepository.returnEquipment(id,EquipmentStatus.withName(status)).map {
      case (equipmentAllocation, equipment) => {
        {

          val kafkaMessage:MessageSchema=(equipmentAllocation,equipment,RETURN)
          kafkaProducerService.sendMessage("key",Json.toJson(kafkaMessage).toString())
          println(equipmentAllocation)
          Ok(Json.obj("message" -> "Equipment returned successfully"))
        }
      }
    }.recover{
      //handling the exceptions occuring within the Future returned
      case e: Exception => BadRequest(Json.obj("message" -> e.getMessage))
    }

  }
  catch {
    case e: Exception => Future.successful(BadRequest(Json.obj("message" -> e.getMessage))
    )
  }






}


  def testKafka = Action.async {
    kafkaProducerService.sendMessage( "key", "Testing kafka producer")
    Future(Ok("Message sent to Kafka"))
  }


}
