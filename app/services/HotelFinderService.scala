package services

import javax.inject.{Inject, Singleton}

import model.Coordinates

@Singleton
class HotelFinderService @Inject() (catalogueService: HotelCatalogueService) {

  val allHotels = for {
    (id, hotel) <- catalogueService.hotels
  } yield id -> hotel.coordinates

  private val distance = new {
    //From https://rosettacode.org/wiki/Haversine_formula#Scala
    import math._

    val R = 6372.8  //radius in km

    def haversine(source: Coordinates, target: Coordinates): Double ={
      val Coordinates(lat1, lon1) = source
      val Coordinates(lat2, lon2) = target
      val dLat=(lat2 - lat1).toRadians
      val dLon=(lon2 - lon1).toRadians

      val a = pow(sin(dLat/2),2) + pow(sin(dLon/2),2) * cos(lat1.toRadians) * cos(lat2.toRadians)
      val c = 2 * asin(sqrt(a))
      R * c
    }
  }

  def findHotels(center: Coordinates, radius: Int): Map[Long, Coordinates] = {
    for {
      (id, coordinates) <- allHotels
      dist = distance.haversine(center, coordinates)
      if dist < radius
    } yield id -> coordinates
  }.toMap

}
