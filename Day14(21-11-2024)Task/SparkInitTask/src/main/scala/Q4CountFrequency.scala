import org.apache.spark.api.java.JavaSparkContext.fromSparkContext
import org.apache.spark.{SparkConf, SparkContext}

object Q4CountFrequency {
  def main(args:Array[String]):Unit={

    /**
     * Write a Spark program to count the frequency of each character in a given collection of strings using RDD transformations.
     */
    val conf=new SparkConf().setAppName("CountFrequency").setMaster("local[*]")
    val sc=new SparkContext(conf)
    val data=Seq("hello world","hello hello world","hello world world")
    val rdd=sc.parallelize(data)
    val chars=rdd.flatMap(_.split(" ")).flatMap(_.split("")).map((_,1)).reduceByKey(_+_)
    val result=chars.collect()
//    result.foreach(p=>{
//
//    })
    result.foreach(println)

    sc.close()
  }
}
