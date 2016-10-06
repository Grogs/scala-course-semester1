package model

case class Coordinates(lat: Double, long: Double)

case class Hotel(id: Long, name: String, coordinates: Coordinates, images: Seq[String])
