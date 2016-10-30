package views

import model.{Coordinates, Hotel}

import scalatags.Text.all._

object HotelsTable {

  def link(coordinates: Coordinates) = {
    import coordinates._
    s"http://maps.google.com/?q=$lat,$long"
  }


  def apply(hotels: Seq[Hotel]) =
    table( id := "hotels", cls := "table",
      thead(
        tr(
          th("Name"),
          th("Location"),
          th("Image")
        )
      ),
      tbody(
        for (h <- hotels) yield
          tr(
            td(h.name),
            td(
              a(href := link(h.coordinates), "See Map")
            ),
            td(
              for (i <- h.smallImages) yield
                img(src := i)
            )
          )
      )
    )
}
