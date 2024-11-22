import org.apache.spark.api.java.JavaSparkContext.fromSparkContext
import org.apache.spark.{SparkConf, SparkContext}

object Q5averagetuples {
  def main(args: Array[String]): Unit = {

    val conf=new SparkConf().setAppName("AverageTuples").setMaster("local[*]")
    val sc=new SparkContext(conf)
    val data=Seq((1,2),(3,4),(5,6),(7,8),(9,12))
    val rdd=sc.parallelize(data)
    val result=rdd.map((x)=>x._2).reduce(_+_).toDouble/rdd.count()
    println(s"The average is $result")
    sc.close()

  }
}
