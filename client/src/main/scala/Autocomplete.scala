import org.scalajs.dom.ext.PimpedHtmlCollection
import org.scalajs.dom.html.Input
import org.scalajs.dom.raw.{Event => DOMEvent}

import scala.concurrent.Future
import scala.scalajs.js.debugger
import scalatags.JsDom.all._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Requires Bootstrap CSS to be in scope
  */
class Autocomplete(input: Input, values: Seq[String], select: String => Unit) {

  sealed trait Event
  case class Select(value: String) extends Event
  case class Change(value: String) extends Event
  case object Show extends Event
  case object Hide extends Event


  val suggestions =
    ul( `class`:="dropdown-menu", id:=input.id+"-autcomplete", style:=s"left: ${input.offsetLeft}px; top: ${input.offsetTop + input.offsetHeight}px; position: absolute; z-index: 100; display: block;  bottom: auto;",
      for (v <- values) yield {
        li( onmousedown := (() => handle(Select(v))),
          a(v)
        )
      }
    ).render

  def handle(e: Event): Unit = {
    def show() = suggestions.style.display = "block"
    def hide() = suggestions.style.display = "none"
    e match {
      case Select(value) =>
        hide()
        input.value = value
        select(value)
      case Change("") =>
        handle(Show)
      case Change(nonEmpty) =>
        for (s <- suggestions.children)
          if (s.textContent.toLowerCase contains nonEmpty.toLowerCase)
            s.classList.remove("hidden")
          else
            s.classList.add("hidden")
        show()
      case Show =>
        suggestions.children.foreach(_.classList.remove("hidden"))
        show()
      case Hide =>
        hide()
    }
  }


  input.parentNode.insertBefore(suggestions, input.nextSibling)
  input.autocomplete = "off"
  input.oninput = (e: DOMEvent) => handle(Change(input.value))
  input.onfocus = (e:DOMEvent) => handle(Show)
  input.onblur = (e:DOMEvent) => { handle(Hide); false}
  handle(Hide)

}
