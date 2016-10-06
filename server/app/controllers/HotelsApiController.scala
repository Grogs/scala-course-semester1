package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.hotels.HotelsService
import model._

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

}
