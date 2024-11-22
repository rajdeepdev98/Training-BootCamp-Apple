import org.apache.spark.{SparkConf, SparkContext}

object Q2CartesianProduct {

  def main(args:Array[String]):Unit={

    /***
     *  Create two RDDs containing numbers and write a Spark program to compute their Cartesian product using RDD transformations.
     */
    val conf=new SparkConf().setAppName("CartesianProduct").setMaster("local[*]")
    val sc=new SparkContext(conf)

    val rdd1=sc.parallelize(Seq(1,2,3,4,5))
    val rdd2=sc.parallelize(Seq(6,7,8,9,10))
    val rdd3=rdd1.cartesian(rdd2)
    val result=rdd3.collect()
    println(result.toList )
    sc.stop()


  }
}
