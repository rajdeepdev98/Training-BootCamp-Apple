package org.training.spark

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.Seq

object Q4 {

  def main(args:Array[String]):Unit={

    println("Starting task4")
    val conf=new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc=new SparkContext(conf)

    val rdd1=sc.parallelize(1 to 10000) //creating the first rdd

    val rdd2=rdd1.filter(_%2==0)//getting the even numbers

    val rdd3=rdd2.map(_*10)//Multiplying each number by 10

    val rdd4=rdd3.flatMap(x=>Seq((x,x+1)))//getting a sequence of two numbers by flatMap

//    val rdd6=rdd4.reduce((x,y)=>(x._1+y._1,x._2+y._2))//reducing the sequence of two numbers to a single tuple

    val rdd5=rdd4.reduceByKey(_+_)
    val rdd6=rdd4.map(x=>(x._1*2,x._2*2))
    val rdd7=rdd6.collect()

    val results = rdd5.collect()

    // Print the results
    results.foreach(println)





    Thread.sleep(Long.MaxValue)
    sc.stop()







  }

}
