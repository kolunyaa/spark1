package com.zerniuk

import java.text.SimpleDateFormat

import scala.util.Try

object TrainHandler {

  def validate(renfe: Renfe) = {
    and(
      validateDate(renfe.insert_date),
      validateDate(renfe.start_date),
      validateDate(renfe.end_date),
    )
  }

  def clean(renfe: Renfe): Boolean = {
    and(
      cleanTrainType(renfe.train_type),
      cleanTrainClass(renfe.train_class),
      renfe.price.nonEmpty
    )
  }

  def format(acp: AvgClassPrice) = {
    val current = acp.average_price
    acp.copy(average_price = f"$current%2.2f".toDouble)
  }

  private def validateDate(date: String) = Try(dateFormat.parse(date)).isSuccess

  private def cleanTrainClass(trainClass: String) = {
    Option(trainClass).exists(validClasses.contains(_))
  }

  private def cleanTrainType(trainType: String) = {
    Option(trainType).exists(validTrainTypes.contains(_))
  }

  private def and(booleans: Boolean*) = {
    booleans.reduce(_ && _)
  }

  val fileFormat = "com.databricks.spark.csv"
  val fileUrl = "s3a://bucket/renfe.csv"
  val outputPath = "s3a://bucket/result"

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


  case class Renfe(id: String, insert_date: String,
                   origin: String, destination: String, start_date: String,
                   end_date: String, train_type: String, price: Option[Double],
                   train_class: String, fare: String)

  case class AvgClassPrice(train_class: String, train_type: String, average_price: Double)

  private val validTrainTypes = Seq(
    "AVE-LD",
    "R. EXPRES",
    "AV City",
    "AVE",
    "INTERCITY",
    "MD-LD",
    "MD-AVE",
    "REGIONAL",
    "AVE-MD",
    "AVE-TGV",
    "ALVIA")
  /*
        "LD-MD",
        "MD",
        "LD",
        "TRENHOTEL" */

  private val validClasses = Seq("Turista Plus",
    "Turista",
    "Preferente",
    "Turista con enlace",
    "Cama Turista"
    //      "Cama G. Clase" assume we don't need this type anymore
  )

}
