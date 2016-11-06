package controllers

import javax.inject.Inject

import play.api.mvc._
import services.hotels.HotelsService

import play.api.Logger

class HotelsController @Inject() (webJarAssets: WebJarAssets, hotelsService: HotelsService) extends Controller {

  val googleMapsApiKey = Option(System.getenv("GOOGLE_MAPS_API_KEY"))

  def hotelListings(destination: Option[String], distance: Option[Float]) = Action{

    val hotels = for {
      dest <- destination
      dist <- distance
    } yield hotelsService.search(dest, dist)

    Ok(views.html.hotelListings(webJarAssets, googleMapsApiKey)(destination, distance, hotels))
  }

  def hotelBooking(hotelId: String) = Action{
    hotelsService.get(hotelId.toLong) match {
      case Some(hotel) => Ok(views.html.hotelBooking(webJarAssets)(hotel))
      case None => NotFound(s"No hotel found for ID: $hotelId")
    }
  }

  def book() = Action{ req => {
    val form = req.body.asFormUrlEncoded.get.mapValues(_.head)
    val List(hotelId, checkin, checkout) = List("hotelId", "checkin", "checkout").map(form)
    Logger.info(s"Booking received for hotelID: $hotelId, checkin: $checkin, checkout: $checkout")
    Ok("Booked")
  }}

}
