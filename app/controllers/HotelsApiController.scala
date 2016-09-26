package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.hotels.{GeographyService, HotelCatalogueService, HotelFinderService, HotelsService}
import services.{HotelCatalogueService, HotelFinderService}

class HotelsApiController @Inject()(hotelsService: HotelsService) extends Controller {

  def hotelsNear(destination: String, rawRadius: String) = Action {
    val radius = rawRadius.toDouble
    Ok(
      Json.toJson(
        hotelsService.search(destination, radius)
      )
    )
  }

}
