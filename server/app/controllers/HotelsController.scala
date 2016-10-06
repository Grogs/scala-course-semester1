package controllers

import javax.inject.Inject

import play.api.mvc._

class HotelsController @Inject() () extends Controller {

  def landingPage() = Action(
    Ok(views.html.landingPage())
  )

}
