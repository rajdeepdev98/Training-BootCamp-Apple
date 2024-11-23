package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}
import security.JwtUtil

case class LoginRequest(username: String, password: String)

object LoginRequest {
  implicit val format: Format[LoginRequest] = Json.format[LoginRequest]
}

@Singleton
class AuthController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def login: Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[LoginRequest].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> "Invalid request format"))),
      loginRequest => {
        // Replace with actual user validation logic
        if (loginRequest.username == "Admin" && loginRequest.password == "Password@12345") {
          val token = JwtUtil.generateToken(loginRequest.username)
          Future.successful(Ok(Json.obj("token" -> token)))
        } else {
          Future.successful(Unauthorized(Json.obj("message" -> "Invalid credentials")))
        }
      }
    )
  }

  def logout: Action[AnyContent] = Action { implicit request =>
    // Invalidate the token (client-side removal)
    Ok(Json.obj("message" -> "Logged out"))
  }
}