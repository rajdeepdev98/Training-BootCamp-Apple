package org.training.spark

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.Seq
import scala.util.Random

object RandomTextGenerator {

  def main(args:Array[String]):Unit={
    println("Generating random strings....")

    val conf = new SparkConf().setAppName("RandomTextGenerator").setMaster("local[*]")
    val sc = new SparkContext(conf)

    // Function to generate a random string of a given length
    def randomString(length: Int): String = {
      val chars = "abcdefghijklmnopqrstuvwxyz"
      (1 to length).map(_ => chars(Random.nextInt(chars.length))).mkString
    }

    // Create an RDD with 1 million lines of random text
    val rdd = sc.parallelize(Seq.fill(1000000)(randomString(10)))


    // Save the RDD to a text file
    rdd.saveAsTextFile("/Users/RajdeepDeb/Documents/Training Bootcamp/Day15Task/Day15task/inputfiles/random_text_output")

    Thread.sleep(Long.MaxValue)
    sc.stop()
  }

}
