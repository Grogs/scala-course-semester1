package model

case class Coordinates(lat: Double, long: Double)

case class Hotel(id: Long, name: String, coordinates: Coordinates, smallImages: Seq[String],
                 largeImages: Seq[String], phoneNumber: String, starRating: Double,
                 descriptionHtml: String, guestRating: Double, numberOfReviews: Int)