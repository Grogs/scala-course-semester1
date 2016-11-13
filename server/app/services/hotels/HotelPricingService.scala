package services.hotels

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.{Future, Promise, blocking}
import scala.concurrent.ExecutionContext.Implicits.global

class HotelPricingService @Inject() (ws: WSClient) {
    case class Room(name: String, descriptionHtml: String)
    case class RoomSummary(room: Room, pricePerNight: Double, totalPrice: Double)
    case class Response(cheapest: RoomSummary, available: Seq[RoomSummary])

    def getprices(hotelId: String, from: LocalDate, to: LocalDate): Future[Response] = {


        implicit val roomReads = Json.reads[Room]
        implicit val roomSummaryReads = Json.reads[RoomSummary]
        implicit val responseReads = Json.reads[Response]

        val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val f = from.format(dtf)
        val t = to.format(dtf)

        ws.url(s"http://http://cdceb930.ngrok.io/prices/$hotelId?from=$f&to=$t")
          .get()
          .map { resp =>
              resp.json.as[Response]
          }
    }

}
