package controllers

import javax.inject.Inject

import play.api.mvc._
import services.hotels.HotelsService

import play.api.Logger

class HotelsController @Inject() (webJarAssets: WebJarAssets, hotelsService: HotelsService) extends Controller {

  def hotelListings(destination: Option[String], distance: Option[Float]) = Action{

    val hotels = for {
      dest <- destination
      dist <- distance
    } yield hotelsService.search(dest, dist)

    Ok(views.html.hotelListings(webJarAssets)(destination, distance, hotels))
  }

}
