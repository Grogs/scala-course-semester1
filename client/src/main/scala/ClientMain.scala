import org.scalajs.dom._
import org.scalajs.dom.document.location
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.html.{Button, Input}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, ScalaJSDefined}
import scala.scalajs.js.{Dynamic, JSApp}

object ClientMain extends JSApp {

    @JSExport
    override def main(): Unit = {
        println("Application initiated")
        val destinationInput = document.getElementById("destination").asInstanceOf[Input]
        val distanceInput = document.getElementById("distance").asInstanceOf[Input]
        val loadButton = document.getElementById("load-hotels").asInstanceOf[Button]
        val hotelTable = document.getElementById("hotels")

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
        def reload(destination: String, distance: String, pushState: Boolean = true) =
            Ajax.get(s"/hotels?destination=$destination&distance=$distance", responseType = "document").map( xhr =>
                xhr.responseXML.getElementById("hotels")
            ).foreach { newListings =>
                if (pushState) {
                    val path = location.pathname + s"?destination=$destination&distance=$distance"
                    val state = Dynamic.literal(destination = destination, distance = distance)
                    window.history.pushState(state, "", path)
                }
                hotelTable.innerHTML = newListings.innerHTML
            }

    }

    @ScalaJSDefined
    trait State extends js.Any {
        val destination: String
        val distance: String
    }
}
