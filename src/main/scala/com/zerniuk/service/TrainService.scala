package com.zerniuk.service

import java.text.SimpleDateFormat

import com.zerniuk.dto.{AvgClassPrice, Renfe}

import scala.util.Try

object TrainService {

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
