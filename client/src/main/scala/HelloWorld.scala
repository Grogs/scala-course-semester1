import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object HelloWorld extends JSApp {

    @JSExport
    override def main(): Unit = {
        println("Hello World!")
    }
}
