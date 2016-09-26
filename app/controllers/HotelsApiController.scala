package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.{GeographyService, HotelCatalogueService, HotelFinderService}

class HotelsApiController @Inject() (
                           catalogueService: HotelCatalogueService,
                           geographyService: GeographyService,
                           hotelFinderService: HotelFinderService) extends Controller {

    def hotelsNear(destination: String, radius: String) = Action{
        Ok (
            Json.toJson(
                for {
                    coordinates <- geographyService.lookupDestination(destination).toSeq
                    hotelId <- hotelFinderService.findHotels(coordinates, radius.toDouble)
                    hotel <- catalogueService.lookupHotel(hotelId)
                } yield hotel
            )
        )
    }

}
