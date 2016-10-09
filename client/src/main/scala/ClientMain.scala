import scala.scalajs.js.{JSApp, debugger}
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom._
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.html.{Button, Input}
import scala.concurrent.ExecutionContext.Implicits.global

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
            s => reload(s, distanceInput.value)
        )

        distanceInput.onchange = (e: Event) => reload(destinationInput.value, distanceInput.value)
        loadButton.style.display = "none"

        //Poor man's PJAX
        def reload(destination: String, distance: String) =
            Ajax.get(s"/hotels?destination=$destination&distance=$distance", responseType = "document").map( xhr =>
                xhr.responseXML.getElementById("hotels")
            ).foreach( newListings =>
                hotelTable.innerHTML = newListings.innerHTML
            )

    }

}
