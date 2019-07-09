package com.zerniuk.dto

case class Renfe(id: String, insert_date: String,
                 origin: String, destination: String, start_date: String,
                 end_date: String, train_type: String, price: Option[Double],
                 train_class: String, fare: String)
