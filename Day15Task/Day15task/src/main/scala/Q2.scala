package org.training.spark

import org.apache.spark.{SparkConf, SparkContext}

object Q2 {

  def main(args: Array[String]): Unit = {

    println("Starting task 2")
    val conf=new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc=new SparkContext(conf)

    //creating Rdd of numbers from 1 to 1000
    val rdd1=sc.parallelize(1 to 1000)
    //applying narrow transformations
    val rdd2=rdd1.filter(_%2==0)
    val rdd3=rdd2.map(x=>(x%10,x))
    //apply a wide transformation ->groupByKey or reduceByKey

    val rdd4=rdd3.reduceByKey(_+_)

    rdd4.saveAsTextFile("/Users/RajdeepDeb/Documents/Training Bootcamp/Day15Task/Day15task/Outputfiles/output")

    Thread.sleep(Long.MaxValue)

    sc.stop()


  }

}
