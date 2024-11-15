package controllers

import model.Equipment
import play.api.libs.json._
import play.api.mvc._
import repository.EquipmentRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
//import models.Equipment

@Singleton
class EquipmentController @Inject()(cc: ControllerComponents,equipmentRepository:EquipmentRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  implicit val jsonFormat = Json.format[Equipment]
  // In-memory list of equipment (this is just for demo purposes, no database involved)
  def list() = Action.async {

    equipmentRepository.list().map(equipments => Ok(Json.toJson(equipments)))
  }

  def getById(id:Long)=Action.async{
    equipmentRepository.getById(id).map{
      case Some(equipment)=>Ok(Json.toJson(equipment))
      case None=>NotFound(Json.obj("message"->"Equipment not found"))
    }
  }
  def add()=Action.async(parse.json){
      request=>{

        request.body.validate[Equipment].fold(
          errors=>Future.successful(BadRequest(Json.obj("message"->JsError.toJson(errors)))),
          equipment=>{
            equipmentRepository.add(equipment).map(equipment=>Created(Json.toJson(equipment)))
          }
        )

    }
  }
  def update(id:Long)=Action.async(parse.json){
    request=>{
      request.body.validate[Equipment].fold(
        errors=>Future.successful(BadRequest(Json.obj("message"->JsError.toJson(errors)))),
        equipment=>{
          equipmentRepository.update(equipment).map{
            case 0=>NotFound(Json.obj("message"->"Equipment not found"))
            case _=>Ok(Json.obj("message"->"Equipment updated successfully"))
          }
        }
      )
    }
  }


  def delete(id:Long)=Action.async{
    equipmentRepository.delete(id).map{
      case 0=>NotFound(Json.obj("message"->"Equipment not found"))
      case _=>Ok(Json.obj("message"->"Equipment deleted successfully"))
    }
  }

  // End point for testing Futures
  def testFuture() = Action.async{
    println(s"${Thread.currentThread().getName}- Handling request")
    Future{
      println(s"${Thread.currentThread().getName}- Performing some computation")
      Thread.sleep(5000)
      Ok("Returning Future")
    }
//    Ok("Returning 2nd Future")
  }

}

