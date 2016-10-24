package services.hotels

import model.Hotel

trait HotelsServiceApi {

    def search(destination: String, radius: Double): Seq[Hotel]

}
