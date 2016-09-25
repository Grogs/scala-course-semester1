package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.libs.json.Writes._
import play.api.mvc._
import services.{GeoService, HotelCatalogueService, HotelFinderService}

class HotelsApiController @Inject()(
  geoService: GeoService,
  finderService: HotelFinderService,
  catalogueService: HotelCatalogueService
) extends Controller {

  def hotels(destination: String, radiusRaw: String) = Action {
    val radius = radiusRaw.toDouble
    Ok {
      Json.toJson(
        for {
          coordinates <- geoService.lookupCoordinates(destination).toSeq
          (hotelId, _) <- finderService.findHotels(coordinates, radius)
          hotel <- catalogueService.lookupHotel(hotelId)
        } yield hotel
      )
    }
  }


}
