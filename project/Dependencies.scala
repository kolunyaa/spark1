import sbt._

object versions {
  val spark = "2.4.3"
  val hadoop = "2.8.5"
}

object Dependencies {
  val sql = "org.apache.spark" %% "spark-sql" % versions.spark force()
  val minio = "io.minio" % "minio" % "2.0.1"
  val hadoopAws = "org.apache.hadoop" % "hadoop-aws" % versions.hadoop force()

  val scalatest = "org.scalatest" %% "scalatest" % "3.0.6" % Test
  val junit = "junit" % "junit" % "4.12" % Test


  val spark = Seq(sql, hadoopAws)
  val additional = Seq(minio)
  val testLibs = Seq(scalatest, junit)
}
