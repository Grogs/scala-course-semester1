package services.hotels

import java.io.{File, FilenameFilter}
import javax.inject.Singleton

import model.{Coordinates, Hotel}
import play.api.libs.json.{JsArray, JsDefined, Json}

import scala.io.Source

@Singleton
class HotelCatalogueService {

  val hotels: Map[Long, Hotel] = {
    for {
      file <- new File(getClass.getResource("/example-hotels").toURI).listFiles(new FilenameFilter {
        def accept(dir: File, name: String): Boolean = name endsWith ".json"
      })
      body = Source.fromFile(file).mkString
      json = Json.parse(body)
      id = (json \ "id").as[Long]
      JsDefined(JsArray(imageReferences)) = json \ "imageReferences"
      largeImages = imageReferences.flatMap(ref => (ref \ "images" \ "Y_500_VARIABLE" \ "url").asOpt[String])
      smallImages = imageReferences.flatMap(ref => (ref \ "images" \ "E_160x90" \ "url").asOpt[String])
      lat = (json \ "coordinate" \ "latitude").as[Double]
      long = (json \ "coordinate" \ "longitude").as[Double]

      phoneNumber = (json \ "phoneNumber").as[String]
      starRating = (json \ "starRating").as[Double]
      descriptionHtml = (json \ "description").as[String]
      guestRating = (json \ "guestRating").as[Double]
      numberOfReviews = (json \ "numberOfReviews").as[Int]
    } yield id -> Hotel(
      id,
      (json \ "name").as[String],
      Coordinates(lat, long),
      smallImages.take(3).map("http://exp.cdn-hotels.com" + _),
      largeImages.take(3).map("http://exp.cdn-hotels.com" + _),
      phoneNumber,
      starRating,
      descriptionHtml,
      guestRating,
      numberOfReviews
    )
  }.toMap


  def lookupHotel(id: Long): Option[Hotel] =
    hotels.get(id)

}