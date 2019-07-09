package com.zerniuk.service

import com.zerniuk.service.MinioService._
import io.minio.MinioClient

class MinioService {

  def upload(path: String, name: String) = {
    val minioClient = new MinioClient(url, accessKey, secretKey)
    if (!minioClient.bucketExists(bucket)) minioClient.makeBucket(bucket)

    minioClient.putObject(bucket, name, path)
  }

}

object MinioService {

  val bucket = "bucket"
  val url = "http://127.0.0.1:9000"

  val accessKey = "Q3AM3UQ867SPQQA43P2F"
  val secretKey = "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG"
}
