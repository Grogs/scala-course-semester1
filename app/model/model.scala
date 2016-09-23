package model

import play.api.libs.json.Json


case class Coordinates(lat: Double, long: Double)

case class Hotel(id: Long, name: String, coordinates: Coordinates, images: Seq[String])

object Hotel {
    implicit val coordinates = Json.writes[Coordinates]
    implicit val writes = Json.writes[Hotel]
}

