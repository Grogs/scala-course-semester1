package services.hotels

import java.time.LocalDate

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.ws.WS

class HotelPricingServiceAcceptanceTest extends PlaySpec with OneAppPerSuite with ScalaFutures with IntegrationPatience {

  "HotelPriceService#getPrices" should {
    "return prices" in {
      val pricingService = new HotelPricingService(WS.client)
      val prices = pricingService.getPrices("someId", LocalDate.now, LocalDate.now.plusDays(3))
      println(prices.futureValue)
    }
  }

}
