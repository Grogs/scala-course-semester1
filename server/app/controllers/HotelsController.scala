package controllers

import javax.inject.Inject

import play.api.mvc._

class HotelsController @Inject() (webJarAssets: WebJarAssets) extends Controller {

  def hotelListings() = Action{
    Ok(views.html.hotelListings(webJarAssets))
  }

}
