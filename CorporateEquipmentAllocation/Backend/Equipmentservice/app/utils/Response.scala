package utils

case class Response(message:String)

object Response {
  import play.api.libs.json.Json
  implicit val responseFormat = Json.format[Response]
}