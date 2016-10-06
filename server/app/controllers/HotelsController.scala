package controllers

import javax.inject.Inject

import play.api.mvc._

class HotelsController @Inject() (webJarAssets: WebJarAssets) extends Controller {

  def landingPage() = Action(
    Ok(views.html.landingPage(webJarAssets))
  )

}
