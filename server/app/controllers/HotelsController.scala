package controllers

import javax.inject.Inject

import play.api.mvc._
import services.hotels.HotelsService

class HotelsController @Inject() (webJarAssets: WebJarAssets, hotelsService: HotelsService) extends Controller {

  def landingPage(destination: Option[String], distance: Option[Float]) = Action{

    val hotels = for {
      dest <- destination
      dist <- distance
    } yield hotelsService.search(dest, dist)

    Ok(views.html.landingPage(webJarAssets)(destination, distance, hotels))
  }

}
