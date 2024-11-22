import org.apache.spark.api.java.JavaSparkContext.fromSparkContext
import org.apache.spark.{SparkConf, SparkContext}

object Q6Join {

  def main(args: Array[String]): Unit = {
    /***
     *  Create two RDDs containing key-value pairs `(id, name)` and `(id, score)`.
     *  Write a Spark program to join these RDDs on `id` and produce `(id, name, score)`.
     */
    val conf=new SparkConf().setAppName("JoinRDD").setMaster("local[*]")
    val sc=new SparkContext(conf)

    val data=Seq((1,"Alice"),(2,"Bob"),(3,"Charlie"),(4,"David"),(5,"Eve"))
    val data2=Seq((1,90),(2,80),(3,70),(4,60),(5,50),(5,100))

    val rdd1=sc.parallelize(data)
    val rdd2=sc.parallelize(data2)
    val result=rdd1.join(rdd2)
    result.collect.foreach(println)
    println(result.count())
    sc.close()
  }

}
