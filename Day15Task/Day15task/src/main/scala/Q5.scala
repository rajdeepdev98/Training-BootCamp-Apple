package org.training.spark

import org.apache.spark.{SparkConf, SparkContext}

object Q5 {

  def main(args:Array[String]):Unit={

      println("Starting task 5")
      val conf=new SparkConf().setAppName("Task5").setMaster("local[*]")

      val sc=new SparkContext(conf)

      val rdd1=sc.textFile("/Users/RajdeepDeb/Documents/Training Bootcamp/Day15Task/Day15task/inputfiles/large_file.csv",15)

      println(s"Row count is ${rdd1.count()}")

     val rdd2=rdd1.map(x=>x.split(",")).map(x=>(x(0),x)).sortByKey(true).map(x=>x._2).saveAsTextFile("/Users/RajdeepDeb/Documents/Training Bootcamp/Day15Task/Day15task/outputfiles/CSVOutput")//for descending use "false"

    Thread.sleep(Long.MaxValue)

    sc.stop()

  }

}
