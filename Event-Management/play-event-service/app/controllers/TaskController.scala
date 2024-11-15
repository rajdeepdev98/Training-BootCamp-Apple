package controllers

import models.AssignTasksRequest
import models.entity.Task
import play.api.libs.json._
import play.api.mvc._
import services.TaskService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaskController @Inject()(cc: ControllerComponents, taskService: TaskService)
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {

  // Create a task
  def createTask(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Task] match {
      case JsSuccess(task, _) =>
        taskService.create(task).map(created =>
          Created(Json.toJson(created)))
      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj(
          "message" -> "Invalid Event data",
          "errors" -> JsError.toJson(errors))))
    }
  }

  // Get task details
  def getTaskById(taskId: Long): Action[AnyContent] = Action.async {
    taskService.getEventById(taskId).map(created =>
      Ok(Json.toJson(created)))
  }

  // Update task status
  def updateTaskStatus(taskId: Long): Action[JsValue] = Action.async(parse.json) { request =>
    val newStatus = request.headers.get("newStatus")

    newStatus match {
      case Some(status) =>
        taskService.updateStatus(taskId, status).map { updatedTask =>
          Ok(Json.toJson(updatedTask))
        }.recover {
          case ex: Exception =>
            BadRequest(Json.obj("message" -> s"Error updating task status: ${ex.getMessage}"))
        }
      case None =>
        Future.successful(BadRequest(Json.obj("message" -> "newStatus header is required")))
    }
  }

  // Assign tasks
  def assignTasks(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[AssignTasksRequest] match {
      case JsSuccess(req, _) =>
        taskService.assignTasks(req).map(created =>
          Created(Json.toJson(created)))
      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj(
          "message" -> "Invalid Event data",
          "errors" -> JsError.toJson(errors))))
    }
  }

}
