package controllers

import javax.inject.Inject

import play.api.mvc._
import services.hotels.HotelsService

class HotelsController @Inject() (webJarAssets: WebJarAssets, hotelsService: HotelsService) extends Controller {

  def hotelListings(destination: Option[String], distance: Option[Float]) = Action{

    val hotels = for {
      dest <- destination
      dist <- distance
    } yield hotelsService.search(dest, dist)

    Ok(views.html.hotelListings(webJarAssets)(destination, distance, hotels))
  }

  def elementalHotelListings() = Action{
    import scalatags.Text.all._
    def url(webjarResource: String) = routes.WebJarAssets.at(webJarAssets.locate(webjarResource)).url
    Ok(
      html(
        head(
          script(src := url("require.min.js")),
          script(src := url("lib/Elemental.js")),
          script(src := "assets/client-fastopt.js"),
          link( rel:="stylesheet", href:= "assets/stylesheets/main.css")
        ),
        body(
//          routes.WebJarAssets.at(webJarAssets.locate("less/main.less")).url
//          scalajs.html.scripts("client", routes.Assets.versioned(_).toString, name => getClass.getResource(s"/public/$name") != null)
        )
      ).render
    ).as(HTML)
  }

}
