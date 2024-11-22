import org.apache.spark.api.java.JavaSparkContext.fromSparkContext
import org.apache.spark.{SparkConf, SparkContext}

object Q1CountWords {

  def main(args: Array[String]): Unit = {
    /***
     * Given a collection of strings, write a Spark program to count the total number of words in the collection using RDD transformations and actions.
      */

      val conf=new SparkConf().setAppName("WordCount").setMaster("local[*]")
      val sc=new SparkContext(conf)

      val data=Seq("hello world","hello hello world","hello world world")
      val rdd=sc.parallelize(data)
      val words=rdd.flatMap(_.split(""))
//      val wordCount=words.map((_,1)).reduceByKey(_+_)
      println(s"The number of words is${words.count()}")

      sc.close( )

  }

}
