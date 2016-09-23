package services

import javax.inject.Singleton

import model.Coordinates

@Singleton
class GeoService {

  private val geoData = Map(
    "London" -> Coordinates(51.507351, -0.127758),
    "Paris" -> Coordinates(48.856614, 2.352222),
    "Bath" -> Coordinates(51.375801, -2.359904),
    "Birmingham" -> Coordinates(52.486243, -1.890401)
  )

  def lookupCoordinates(destination: String): Option[Coordinates] =
    geoData.get(destination)

}
