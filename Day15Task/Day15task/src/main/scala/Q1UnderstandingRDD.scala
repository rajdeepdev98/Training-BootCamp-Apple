package org.training.spark

object Q1UnderstandingRDD {

  def main(args: Array[String]): Unit = {
    println("Starting task 1")

    import org.apache.spark.sql.SparkSession
    import org.apache.spark.rdd.RDD
    import scala.util.Random

    Thread.sleep(10000)

    // Start a Spark session
    val spark = SparkSession.builder
      .appName("Understanding RDD and Partitioning")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext
    Thread.sleep(10000)

    // Create an RDD with millions of random numbers
    val largeRDD: RDD[Int] = sc.parallelize(Seq.fill(1000000)(Random.nextInt(1000)))

    // Check the number of partitions for the RDD
    println(s"Number of partitions: ${largeRDD.getNumPartitions}")

    // Repartition the RDD into 4 partitions
    val repartitionedRDD = largeRDD.repartition(4)
    println(s"Number of partitions after repartitioning: ${repartitionedRDD.getNumPartitions}")

    // Perform an action to trigger the repartitioning
    repartitionedRDD.count()

    // Coalesce the RDD back into 2 partitions
    val coalescedRDD = repartitionedRDD.coalesce(2)
    println(s"Number of partitions after coalescing: ${coalescedRDD.getNumPartitions}")

    // Perform an action to trigger the coalescing
    coalescedRDD.count()

    // Stop the Spark session
//    spark.stop()
    Thread.sleep(Long.MaxValue)

  }

}
