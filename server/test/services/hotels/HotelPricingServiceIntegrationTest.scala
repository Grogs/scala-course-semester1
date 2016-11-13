package services.hotels

import java.time.LocalDate

import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.{OneAppPerSuite, OneAppPerTest, PlaySpec}
import play.api.libs.ws.WS

/**
  * Created by grdorrell on 11/7/16.
  */
class HotelPricingServiceIntegrationTest extends PlaySpec with OneAppPerTest with ScalaFutures {
    )

    val pricingService = new HotelPricingService(WS.client())

    "HotelPricingService" should {
        "getprices" in {
            val eventualResponse = pricingService.getprices("12345", LocalDate.now, LocalDate.now.plusDays(4))
            val response = eventualResponse.futureValue
            response.available.size should be > (0)
        }
    }

}
