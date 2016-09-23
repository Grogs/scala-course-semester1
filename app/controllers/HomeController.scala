package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._
import services.{GeoService, HotelCatalogueService, HotelFinderService}
import play.api.libs.json.Writes._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (
  geoService: GeoService,
  finderService: HotelFinderService,
  catalogueService: HotelCatalogueService
) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def hotelsNearLondon = Action{
    Ok{
      Json.toJson(
        for {
          coordinates <- geoService.lookupCoordinates("London").toSeq
          (hotelId, _) <- finderService.findHotels(coordinates, 2)
          hotel <- catalogueService.lookupHotel(hotelId)
        } yield hotel
      )
    }
  }

}
