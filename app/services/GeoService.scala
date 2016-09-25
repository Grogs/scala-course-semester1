package services

import javax.inject.Singleton

import model.Coordinates

@Singleton
class GeoService {

  private val geoData = Map(
    "london" -> Coordinates(51.507351, -0.127758),
    "paris" -> Coordinates(48.856614, 2.352222),
    "bath" -> Coordinates(51.375801, -2.359904),
    "birmingham" -> Coordinates(52.486243, -1.890401)
  )

  def lookupCoordinates(destination: String): Option[Coordinates] =
    geoData.get(destination.toLowerCase)

}
