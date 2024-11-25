package org.training.spark

import org.apache.spark.{SparkConf, SparkContext}

object Q3 {

  def main(args:Array[String]):Unit={

    println("Starting task 3")
    val conf=new SparkConf().setAppName("WordCount").setMaster("local[*]").set("spark.executor.instances", "2")
    val sc=new SparkContext(conf)

    val rdd1=sc.textFile("/Users/RajdeepDeb/Documents/Training Bootcamp/Day15Task/Day15task/inputfiles/repetitive_text_output")

    val rdd2=rdd1.flatMap(x=>x.split(" ")).map((_,1)).reduceByKey(_+_)

    val result=rdd2.collect()//this would trigger the transformations as per the DAG
    result.foreach(println)



    Thread.sleep(Long.MaxValue)

    sc.stop()
  }

}
