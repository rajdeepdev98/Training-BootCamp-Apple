package org.training.spark

import org.apache.spark.{SparkConf, SparkContext}

object repetitiveTextGenerator {

  def main(args: Array[String]): Unit = {

    println("Generating a repetitive String...")

    val str="Hey there this is Repetitive Text Generator"

    val conf=new SparkConf().setAppName("RepetitiveTextGenerator").setMaster("local[*]")

    val sc=new SparkContext(conf)

    val rdd1=sc.parallelize(Seq.fill(1000000)(str))

    rdd1.saveAsTextFile("/Users/RajdeepDeb/Documents/Training Bootcamp/Day15Task/Day15task/inputfiles/repetitive_text_output")

  }

}
