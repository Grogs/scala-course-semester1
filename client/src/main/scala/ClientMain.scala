import org.scalajs.dom._
import org.scalajs.dom.document.location
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.html.{Button, Input}
import services.hotels.HotelsServiceApi

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, ScalaJSDefined}
import scala.scalajs.js.{Dynamic, JSApp}
import scalatags.Text.all._
import autowire._

object ClientMain extends JSApp {

    @JSExport
    override def main(): Unit = {
        println("Application initiated")
        val destinationInput = document.getElementById("destination").asInstanceOf[Input]
        val distanceInput = document.getElementById("distance").asInstanceOf[Input]
        val loadButton = document.getElementById("load-hotels").asInstanceOf[Button]
        val hotelTable = document.getElementById("hotels")
        val webSocket = new WebSocket(s"ws://${location.hostname}:${location.port}/WebSocket/user")

        new Autocomplete(
            destinationInput,
            Seq("London", "Paris", "Bath"),
            reload(_, distanceInput.value)
        )

        distanceInput.onchange = (e: Event) => reload(destinationInput.value, distanceInput.value)
        window.onpopstate = (e: PopStateEvent) => {
            val state = e.state.asInstanceOf[State]
            reload(state.destination, state.distance, pushState = false)
        }
        loadButton.style.display = "none"

        //Poor man's PJAX
        def reload(destination: String, distance: String, pushState: Boolean = true) = {

            webSocket.send(destination)

            webSocket.onmessage = (e: MessageEvent) => console.log(e.data.toString)

            Client[HotelsServiceApi].search(destination, distance.toDouble).call().foreach { hotels =>
                if (pushState) {
                    val path = location.pathname + s"?destination=$destination&distance=$distance"
                    val state = Dynamic.literal(destination = destination, distance = distance)
                    window.history.pushState(state, "", path)
                }
                hotelTable.innerHTML =
                  views.HotelsTable(hotels).render
            }
        }
    }

    @ScalaJSDefined
    trait State extends js.Any {
        val destination: String
        val distance: String
    }
}
