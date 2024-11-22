import org.apache.spark.api.java.JavaSparkContext.fromSparkContext
import org.apache.spark.{SparkConf, SparkContext}

object Q3filtereven {

    def  main(args:Array[String]):Unit={

      /***
       * Create an RDD from a list of integers and filter out all even numbers using a Spark program.
       */

      val conf=new SparkConf().setAppName("FilterEven").setMaster("local[*]")
      val sc=new SparkContext(conf)

      val rdd=sc.parallelize(Seq(1,2,3,4,5,6,7,8,9,10))
      val evenRdd=rdd.filter(_%2==0)
      val result=evenRdd.collect()
      println(result.toList)
      sc.close()
    }
}
