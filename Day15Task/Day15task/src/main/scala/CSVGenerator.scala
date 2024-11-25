package org.training.spark

import java.io.PrintWriter
import scala.collection.immutable.Seq
import scala.util.Random

object CSVGenerator {

  def main(args:Array[String]):Unit={

      println("Generating a large CSV file")

    val numRows = 1000000
    val numCols = 10
    val fileName = "/Users/RajdeepDeb/Documents/Training Bootcamp/Day15Task/Day15task/inputfiles/large_file.csv"

    val writer = new PrintWriter(fileName)
    for (_ <- 1 to numRows) {
      val row = Seq.fill(numCols)(Random.nextInt(1000)).mkString(",")
      writer.println(row)
    }
    writer.close()
  }

}
