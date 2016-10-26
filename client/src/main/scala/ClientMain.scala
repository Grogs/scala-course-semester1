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

object ClientMain extends JSApp {

    @JSExport
    override def main(): Unit = {
        println("Hello World!")
        val destinationInput = document.getElementById("destination").asInstanceOf[Input]
        val distanceInput = document.getElementById("distance").asInstanceOf[Input]

        document.getElementById("load-hotels").asInstanceOf[Button].style.display = "none"

        def getDestination = destinationInput.value
        def getDistance = distanceInput.value

        def reload(destination: String = getDestination, distance: String = getDistance, pushSate: Boolean = true) = {
            val hotels:Future[Seq[Hotel]] = null//ApiClient[HotelsServiceApi].search(destination, distance).call()

            hotels.map( h =>
                val oldTable = document.getElementById("hotels")
                oldTable.innerHTML = TableView.render(h).render
            )

            if (pushSate)
                window.history.pushState(literal(destination = destination, distance = distance), "", newPath)
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
