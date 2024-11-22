import org.apache.spark.api.java.JavaSparkContext.fromSparkContext
import org.apache.spark.{SparkConf, SparkContext}

object Q9SumRDDAction {

  def main(args:Array[String]):Unit={

    /***
     * Create an RDD of integers from 1 to 100 and write a Spark program to compute their sum using an RDD action.
     */

    val conf=new SparkConf().setAppName("SumRDDAction").setMaster("local[*]")
      val sc=new SparkContext(conf)
     val data=1 to 100
    println(data)
    val rdd=sc.parallelize(data)

    val sum=rdd.reduce(_+_)
    println(s"The sum of the numbers from 1 to 100 is $sum")
    sc.close()



  }

}
