import org.apache.spark.api.java.JavaSparkContext.fromSparkContext
import org.apache.spark.{SparkConf, SparkContext}

object Q7Union {

  def main(args:Array[String]):Unit={

    /**
     * Write a Spark program to perform a union operation on
     * two RDDs of integers and remove duplicate elements from the resulting RDD.
     */
    val conf=new SparkConf().setAppName("UnionRDD").setMaster("local[*]")
    val sc=new SparkContext(conf)
    val data1=Seq(1,2,3,4,5)
    val data2=Seq(4,5,6,7,8,9)
    val rdd1=sc.parallelize(data1)
    val rdd2=sc.parallelize(data2)
    val result=rdd1.union(rdd2).distinct()
    println("The union of two RDDs with distinct results is:")
    result.collect.foreach(v=>print(v+" "))
    println()
    sc.close()
    }
}
