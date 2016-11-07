package services.hotels

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

import play.api.libs.json.Json
import play.api.libs.ws.WSClient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class HotelPricingService  @Inject() (ws: WSClient) {

  case class Room(name: String, descriptionHtml: String)
  case class RoomWithPrice(room: Room, totalPrice: Double, pricePerNight: Double)
  case class Response(available: Seq[RoomWithPrice], cheapest: RoomWithPrice)

  implicit val roomReads = Json.reads[Room]
  implicit val roomWithPriceReads = Json.reads[RoomWithPrice]
  implicit val responseReads = Json.reads[Response]

  val dft = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def getPrices(hotelId: String, from: LocalDate, to: LocalDate): Future[Response] = {

    val f = from.format(dft)
    val t = from.format(dft)

    ws.url(s"http://localhost:8080/prices/$hotelId?from=$f&to=$t")
      .get()
      .map( resp =>
        resp.json.as[Response]
      )
  }

}
