package services.hotels

import javax.inject.Inject

class HotelsService @Inject() (
                                catalogueService: HotelCatalogueService,
                                geographyService: GeographyService,
                                hotelFinderService: HotelFinderService) extends HotelsServiceApi {

  def search(destination: String, radius: Double) = for {
    coordinates <- geographyService.lookupDestination(destination).toSeq
    hotelId <- hotelFinderService.findHotels(coordinates, radius)
    hotel <- catalogueService.lookupHotel(hotelId)
  } yield hotel

  def get(hotelId: Long) = catalogueService.lookupHotel(hotelId)

}
