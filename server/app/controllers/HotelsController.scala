package controllers

import javax.inject.Inject

import play.api.mvc._
import services.hotels.HotelsService

class HotelsController @Inject() (webJarAssets: WebJarAssets, hotelsService: HotelsService) extends Controller {

  def hotelListings(destinationOpt: Option[String], distanceOpt: Option[String]) = Action{

    val hotels = for {
      destination <- destinationOpt
      distance <- distanceOpt
    } yield hotelsService.search(destination, distance.toDouble)

    Ok(views.html.hotelListings(webJarAssets)(hotels))
  }

}
