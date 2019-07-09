## Practical project #1

### Project requirements
 >Batch branch of Lambda architecture - choose dataset (from provided), <br/>
  create Spark application to apply cleaning, transformation, parsing, validation, enrhichment (if applicable) <br/>
   and one aggregation of data, store to the chosen datastore, create simplistic test <br/>
    to verify pipeline sanity (unit tests for parsing, validation and enrichment) <br/>

### Dependencies

     Name         | Version
     ------------ | -------------
     Scala        | 2.12.8
     ScalaTest    | 3.0.6
     JUnit        | 4.12
     Sbt          | 1.2.8
     Spark SQL    | 2.4.3
     Hadoop AWS   | 2.8.5
     Minio        | 2.0.1 
     
### Project description

This project demonstrates the simple data processing using Apache Spark

Dataset: 

`data` folder contains `renfe.csv` file 
which is a short version of <br/>
`Spanish High Speed Rail tickets pricing - Renfe` <br/>

https://www.kaggle.com/thegurus/spanish-high-speed-rail-system-ticket-pricing

App flow:

Main class `com.zerniuk.SparkApp.scala` 

* Uploads dataset file to minio
* Reads dataset from `minio` and maps it to Renfe DTO
* Validation stage checks date `(insert_date, start_date, end_date)` columns format
* Clean stage removes invalid or empty `(train_type, train_class, price)`
* Aggregation stage - calculates average price for each `train_type` and `train_class`
* Format stage rounds `average_price` up to 2 decimal places
* Uploads result to `minio`


### How to run

* Go to docker folder
* run `docker-compose up` command to pull and start `minio` docker container
* Go to `Minio UI` - `http://127.0.0.1:9000/minio/login`
* Use Access and Secret keys in `com.zerniuk.service.MinioService`
* Go to the root of the application and run `sbt compile run`
* Check the result using `Minio UI`  must contain `renfe.csv` and `result` folder
