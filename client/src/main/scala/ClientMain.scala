import org.scalajs.dom.ext.Ajax

import scala.scalajs.js.{Dynamic, JSApp}
import scala.scalajs.js.annotation.{JSExport, JSExportAll, ScalaJSDefined}
import org.scalajs.dom.html.{Button, Div, Input}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal
import services.hotels._
import autowire._
import google.maps.InfoWindowOptions
import model.{Coordinates, Hotel}
import org.scalajs.dom.document.location
import org.scalajs.dom.html.{Button, Input}
import org.scalajs.dom.{raw => _, _}
import org.scalajs.jquery.{JQueryEventObject, jQuery}
import services.hotels.HotelsServiceApi
import views.TableView

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, ScalaJSDefined}
import scala.scalajs.js.{Dynamic, JSApp}
import scalatags.Text.all._

@JSExportAll
object ClientMain extends JSApp {

    override def main(): Unit = {
        js.Dynamic.global.hotelListings = () => hotelListings()
        js.Dynamic.global.hotelBooking = () => hotelBooking()
    }

    def hotelListings() = {

        @ScalaJSDefined
        trait State extends js.Any {
            val destination: String
            val distance: Double
        }

        val destinationInput = document.getElementById("destination").asInstanceOf[Input]
        val distanceInput = document.getElementById("distance").asInstanceOf[Input]
        val loadButton = document.getElementById("load-hotels").asInstanceOf[Button]
        val hotelTable = document.getElementById("hotels")
        val showMapButton = document.getElementById("show-map").asInstanceOf[Button]

        loadButton.style.display = "none"

        def getDestination = destinationInput.value
        def getDistance = distanceInput.value.toDouble

        def reload(destination: String = getDestination, distance: Double = getDistance, pushState: Boolean = true) = {
          Client[HotelsServiceApi].search(destination, distance).call().foreach { hotels =>
            if (hotels.nonEmpty && pushState) {
              val path = location.pathname + s"?destination=$destination&distance=$distance"
              val state = Dynamic.literal(destination = destination, distance = distance)
              window.history.pushState(state, "", path)
            }
            hotelTable.innerHTML = TableView.render(hotels).render
          }
        }

        destinationInput.onkeyup = (e: Event) => reload()
        distanceInput.onkeyup = (e: Event) => reload()
        distanceInput.onchange = (e: Event) => reload()

        // From http://getbootstrap.com/javascript/#modals-events
        jQuery("#mapModal").on("shown.bs.modal",
            (_: JQueryEventObject) => {
                Client[HotelsServiceApi].search(getDestination, getDistance).call().foreach { hotels =>
                    document.getElementById("mapModalLabel").innerHTML = s"Hotels within ${getDistance}km of $getDestination"
                    renderMap(document.getElementById("map"), hotels)
                }
            }
        )

        new Autocomplete(
            destinationInput,
            List("London", "Paris", "Bath"),
            s => reload()
        )

        window.onpopstate = (e: PopStateEvent) => {
            val state = e.state.asInstanceOf[State]
            reload(state.destination, state.distance, false)
        }
    }

    def hotelBooking() = {
        println("Booking page")
    }

    def renderMap(target: Element, hotels: Seq[Hotel]) = {

        val opts = google.maps.MapOptions(
            center = new google.maps.LatLng(50, 0),
            zoom = 11
        )

        val gmap = new google.maps.Map(target, opts)

        val point =
            for {
                hotel <- hotels
                Coordinates(lat, long) = hotel.coordinates
                latLng = new google.maps.LatLng(lat, long)
            } yield {

              val marker = new google.maps.Marker(google.maps.MarkerOptions(
                  position = latLng,
                  map = gmap,
                  title = hotel.name
              ))

              val infoWindow = new google.maps.InfoWindow(
                  InfoWindowOptions( content =
                    div(
                        h2(hotel.name),
                        p(s"Rating: ${hotel.guestRating}/5 (${hotel.numberOfReviews} reviews)"),
                        raw(hotel.descriptionHtml)
                    ).render
                  )
              )

              marker -> infoWindow
            }

        val markerBounds = new google.maps.LatLngBounds()
        var activeInfoWindow = new google.maps.InfoWindow

        for {
            (marker, infoWindow) <- point
        } yield {
            marker.addListener("click", (_:js.Any) => {
                activeInfoWindow.close()
                activeInfoWindow = infoWindow
                infoWindow.open(gmap, marker)
            })
            markerBounds.extend(marker.getPosition())
        }

        gmap.fitBounds(markerBounds)

    }

}
