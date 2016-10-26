package views

import model.{Coordinates, Hotel}

import scalatags.Text.all._

/**
  * Created by grdorrell on 10/24/16.
  */
object TableView {

    def link(coordinates: Coordinates) = {
        import coordinates._
        s"http://maps.google.com/?q=$lat,$long"
    }

    def render(hotels: Seq[Hotel]) = {
        table(id := "hotels", `class` := "table",
            thead(
                tr(
                    th("Name"),
                    th("Location"),
                    th("Image")
                )
            ),
            tbody(
                for (hotel <- hotels) yield {
                    tr(
                        td(hotel.name),
                        td(a(href := link(hotel.coordinates), "See Map")),
                          td(
                          for (i <- hotel.smallImages) yield
                              img(src := i)
                          )
                    )
                }
            )
        )

    }

}
