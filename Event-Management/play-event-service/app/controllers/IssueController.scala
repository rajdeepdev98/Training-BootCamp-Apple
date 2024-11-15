package controllers

import models.entity.Issue
import play.api.libs.json._
import play.api.mvc._
import services.IssueService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class IssueController @Inject()(cc: ControllerComponents, issueService: IssueService)
                               (implicit ec: ExecutionContext) extends AbstractController(cc) {

  // Create an issue
  def create(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Issue] match {
      case JsSuccess(issue, _) =>
        issueService.create(issue).map(id =>
          Created(Json.obj("id" -> id, "message" -> "CREATED")))
      case JsError(errors) =>
        Future.successful(BadRequest(Json.obj(
          "message" -> "Invalid Issue data",
          "errors" -> JsError.toJson(errors))))
    }
  }

  // Get issue by id
  def getIssueById(issueId: Long): Action[AnyContent] = Action.async {
    issueService.getIssueById(issueId).map(created =>
      Ok(Json.toJson(created)))
  }
}
