import org.scalajs.dom.ext.Ajax

import scala.scalajs.js.{Dynamic, JSApp}
import scala.scalajs.js.annotation.{JSExport, ScalaJSDefined}
import org.scalajs.dom._
import org.scalajs.dom.html.{Button, Div, Input}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal
import services.hotels._
import autowire._
import model.Hotel
import views.TableView

import scala.concurrent.Future
import org.scalajs.dom._
import org.scalajs.dom.document.location
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.html.{Button, Input}
import services.hotels.HotelsServiceApi

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, ScalaJSDefined}
import scala.scalajs.js.{Dynamic, JSApp}
import scalatags.Text.all._
import autowire._

object ClientMain extends JSApp {

    @JSExport
    override def main(): Unit = {
        val destinationInput = document.getElementById("destination").asInstanceOf[Input]
        val distanceInput = document.getElementById("distance").asInstanceOf[Input]
        val loadButton = document.getElementById("load-hotels").asInstanceOf[Button]
        val hotelTable = document.getElementById("hotels")

        loadButton.style.display = "none"

        def getDestination = destinationInput.value
        def getDistance = distanceInput.value

        def reload(destination: String = getDestination, distance: String = getDistance, pushState: Boolean = true) = {
          Client[HotelsServiceApi].search(destination, distance.toDouble).call().foreach { hotels =>
            if (pushState) {
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

    @ScalaJSDefined
    trait State extends js.Any {
        val destination: String
        val distance: String
    }
}
