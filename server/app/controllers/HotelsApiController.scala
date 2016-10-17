package controllers

import javax.inject.Inject

import autowire.Core.Request
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.hotels.{HotelsService, HotelsServiceApi}
import model._
import upickle.Js
import upickle.Js.Obj
import upickle.default._
import scala.concurrent.ExecutionContext.Implicits.global


class HotelsApiController @Inject()(hotelsService: HotelsService) extends Controller {

  implicit val coordinates = Json.writes[Coordinates]
  implicit val writes = Json.writes[Hotel]

  def hotelsNear(destination: String, rawRadius: String) = Action {
    val radius = rawRadius.toDouble
    Ok(
      Json.toJson(
        hotelsService.search(destination, radius)
      )
    )
  }

  object ApiServer extends autowire.Server[Js.Value, Reader, Writer] {
    def read[Result: Reader](p: Js.Value) = upickle.default.readJs[Result](p)
    def write[Result: Writer](r: Result) = upickle.default.writeJs(r)
  }

  def api(pathRaw: String) = Action.async { implicit request =>
    val body = request.body.asText.getOrElse("")
    val args = upickle.json.read(body).asInstanceOf[Obj].value.toMap
    ApiServer.route[HotelsServiceApi](hotelsService)(
      Request(pathRaw.split("/"), args)
    ).map( res =>
      Ok(upickle.json.write(res))
    )
  }


}
