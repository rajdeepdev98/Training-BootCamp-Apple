
package security

import org.apache.pekko.stream.Materializer
import play.api.http.HttpFilters
import play.api.libs.json._
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class JWTAuthFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {
  override def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    val publicPaths = List("/login")
    //making sure that the login endpoints are accessible without a token
    if (publicPaths.contains(requestHeader.path)) {
      nextFilter(requestHeader)
    } else {
      println(requestHeader.headers)
      requestHeader.headers.get("Authorization") match {
        case Some(authHeader) if authHeader.startsWith("Bearer ") =>
          val token = authHeader.substring(7)
          JwtUtil.validateToken(token) match {
            case Some(_) => {
              println("testtt")
              nextFilter(requestHeader)
            }
            case None => Future.successful(Results.Unauthorized(Json.obj("error"->"Invalid token")))
          }
        case _ => Future.successful(Results.Unauthorized(Json.obj("error"->"No token provided")))
      }
    }
  }
}
class Filters @Inject()(jwtAuthFilter: JWTAuthFilter) extends HttpFilters {
  override def filters: Seq[EssentialFilter] = Seq(jwtAuthFilter)
}