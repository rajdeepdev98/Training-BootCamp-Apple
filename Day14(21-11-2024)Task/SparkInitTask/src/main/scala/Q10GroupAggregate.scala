import org.apache.spark.api.java.JavaSparkContext.fromSparkContext
import org.apache.spark.{SparkConf, SparkContext}

object Q10GroupAggregate {

  def main(args:Array[String]):Unit={

    /***
     * Write a Spark program to group an RDD of key-value pairs `(key, value)`
     * by key and compute the sum of values for each key.
     */

    val conf=new SparkConf().setAppName("GroupAggregate").setMaster("local[*]")
    val sc=new SparkContext(conf)
    val data = Seq(
      ("Alice", 10),
      ("Bob", 20),
      ("Charlie", 30),
      ("Alice", 40),
      ("David", 50),
      ("Eve", 60),
      ("Frank", 70),
      ("Bob", 80),
      ("Charlie", 90),
      ("David", 100)
    )
    val rdd=sc.parallelize(data)
    val result=rdd.groupByKey().mapValues(_.sum).collect().toList
//    val result2=rdd.groupBy(_._1).collect().toList
//    println(result2)
    println(result)
    sc.close()


  }

}
