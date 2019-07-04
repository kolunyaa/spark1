package com.zerniuk

import com.zerniuk.TrainHandler.{AvgClassPrice, Renfe}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class TrainHandlerTest extends WordSpec with Matchers {

  trait Context {
    val emptyRenfe = Renfe("_", "_", "_", "_", "_", "_", "_", None, "_", "_")
  }

  "TrainHandler" should {
    "validate" should {

      "validate dates" in new Context {

        val invalidInsertDate = emptyRenfe.copy(insert_date = "2019/04/19 05:31:43")
        val invalidStartDate = emptyRenfe.copy(start_date = "2019")
        val invalidEndDate = emptyRenfe.copy(end_date = "2019-05-28")

        val valid = emptyRenfe.copy(
          insert_date = "2019-04-19 05:32:55",
          start_date = "2019-05-29 06:20:30",
          end_date = "2019-05-29 06:21:30"
        )

        val renfes = Seq(invalidInsertDate, invalidStartDate, invalidEndDate, valid)

        val result1 = TrainHandler.validate(invalidInsertDate)
        result1 shouldBe false

        val result2 = TrainHandler.validate(invalidStartDate)
        result2 shouldBe false

        val result3 = TrainHandler.validate(invalidEndDate)
        result3 shouldBe false

        val result4 = TrainHandler.validate(valid)
        result4 shouldBe true

        val validated = renfes.filter(TrainHandler.validate)
        validated.size shouldBe 1
        validated.head shouldBe valid
      }
    }

    "clean train_type and train_class" in new Context {
      val invalidTrainClass = emptyRenfe.copy(train_type = "ALVIA", price = Some(31.44d), train_class = "Cama G. Clase")
      val invalidTrainClass2 = emptyRenfe.copy(train_type = "ALVIA", price = Some(31.44d), train_class = None.orNull)


      val invalidTrainType = emptyRenfe.copy(train_type = "TRENHOTEL", price = Some(31.44d), train_class = "Turista")
      val invalidTrainType2 = emptyRenfe.copy(train_type = None.orNull, price = Some(31.44d), train_class = "Turista")


      val invalidPrice = emptyRenfe.copy(train_type = "ALVIA", price = None, train_class = "Turista")
      val valid = emptyRenfe.copy(train_type = "ALVIA", price = Some(31.44d), train_class = "Turista")

      val result1 = Seq(invalidTrainClass, invalidTrainClass2).filter(TrainHandler.clean)
      result1 shouldBe Seq.empty

      val result2 = Seq(invalidTrainType, invalidTrainType2).filter(TrainHandler.clean)
      result2 shouldBe Seq.empty

      val result3 = TrainHandler.clean(invalidPrice)
      result3 shouldBe false

      val result4 = TrainHandler.clean(valid)
      result4 shouldBe true
    }

    "format average_price" in {
      val avp = AvgClassPrice("Preferente", "ALVIA", 60.0747238)
      val result = TrainHandler.format(avp)
      result.average_price shouldBe 60.07d
    }
  }
}
