package controllers

import javax.inject.Inject

import play.api.mvc.Controller
import services.{GeographyService, HotelCatalogueService, HotelFinderService}

class HotelsApiController @Inject() (
                           catalogueService: HotelCatalogueService,
                           geographyService: GeographyService,
                           hotelFinderService: HotelFinderService) extends Controller {

    def hotelsNear(destination: String, radius: Double) ={
        for {
            coordinates <- geographyService.lookupDestination(destination).toSeq
            hotelId <- hotelFinderService.findHotels(coordinates, radius)
            hotel <- catalogueService.lookupHotel(hotelId)
        } yield hotel
    }

}
