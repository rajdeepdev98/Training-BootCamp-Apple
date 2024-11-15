package controllers

import model.EquipmentAllocation
import play.api.mvc.ControllerComponents
import repository.EquipmentAllocationRepository
import play.api.libs.json._
import play.api.mvc._
import service.KafkaProducerService
import utils.EquipmentStatus.EquipmentStatus

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EquipmentAllocationController @Inject()(cc:ControllerComponents, equipmentAllocationRepository: EquipmentAllocationRepository,kafkaProducerService: KafkaProducerService)(implicit ec:ExecutionContext) extends AbstractController(cc) {


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
          equipmentAllocationRepository.add(equipmentAllocation).map(equipmentAllocation => {
            kafkaProducerService.sendMessage("test", "key", Json.toJson(equipmentAllocation).toString())
            Created(Json.toJson(equipmentAllocation))
          }).recover{
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
  def returnEquipment(id: Long, status: EquipmentStatus) = Action.async {
    equipmentAllocationRepository.returnEquipment(id, status).map {
      case Some(equipmentAllocation) => Ok(Json.toJson(equipmentAllocation))
      case None => NotFound(Json.obj("message" -> "Equipment Allocation not found"))
    }
  }
  def testKafka = Action.async {
    kafkaProducerService.sendMessage("test", "key", "Testing kafka producer")
    Future(Ok("Message sent to Kafka"))
  }


}
