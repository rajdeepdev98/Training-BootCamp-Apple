package controllers

import model.EquipmentAllocation
import play.api.mvc.ControllerComponents
import repository.EquipmentAllocationRepository
import play.api.libs.json._
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class EquipmentAllocationController @Inject()(cc:ControllerComponents, equipmentAllocationRepository: EquipmentAllocationRepository)(implicit ec:ExecutionContext) extends AbstractController(cc) {

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
        equipmentAllocationRepository.add(equipmentAllocation).map(equipmentAllocation => Created(Json.toJson(equipmentAllocation)))
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



}
