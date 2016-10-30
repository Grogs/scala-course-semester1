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
        println("Hello World!")
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

    }

}
