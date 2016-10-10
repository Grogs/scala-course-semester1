import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom.{Event, console, document}
import org.scalajs.dom.html.Input

object ClientMain extends JSApp {

    @JSExport
    override def main(): Unit = {
        println("Hello World!")
        val destinationInput = document.getElementById("destination").asInstanceOf[Input]
        val distanceInput = document.getElementById("distance").asInstanceOf[Input]

        def getDestination() = destinationInput.value
        def getDistance() = distanceInput.value

        def reload() = console.log(getDestination(), getDistance())

        destinationInput.onkeydown = (e: Event) => reload()
        distanceInput.onkeydown = (e: Event) => reload()

    }
}
