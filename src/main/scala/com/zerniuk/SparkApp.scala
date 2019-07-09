package com.zerniuk

import com.zerniuk.dto.{AvgClassPrice, Renfe}
import com.zerniuk.service.MinioService
import com.zerniuk.service.MinioService._
import com.zerniuk.service.TrainService._
import org.apache.spark.sql.SparkSession

object SparkApp extends App {

  val minioService = new MinioService

  val spark = SparkSession
    .builder
    .appName("test")
    .master("local")
    .getOrCreate()

  val sc = spark.sparkContext

  import spark.sqlContext.implicits._

  val config = sc.hadoopConfiguration
  config.set("fs.s3a.endpoint", url)
  config.set("fs.s3a.access.key", accessKey)
  config.set("fs.s3a.secret.key", secretKey)

  spark.conf.set("spark.sql.shuffle.partitions", "5")

  minioService.upload("data/renfe.csv", "renfe.csv")

  spark.read
    .format(fileFormat)
    .option("header", "true")
    .option("inferSchema", "true")
    .load(fileUrl)
    .as[Renfe]
    .filter(validate(_))
    .filter(clean(_))
    .groupBy("train_class", "train_type")
    .avg("price")
    .withColumnRenamed("avg(price)", "average_price")
    .sort("train_class", "train_type")
    .as[AvgClassPrice]
    .map(format)
    .write.format(fileFormat)
    .option("header", "true")
    .save(outputPath)
}
