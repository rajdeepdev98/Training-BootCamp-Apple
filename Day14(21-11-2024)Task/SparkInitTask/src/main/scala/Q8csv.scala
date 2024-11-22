import org.apache.spark.api.java.JavaSparkContext.fromSparkContext
import org.apache.spark.{SparkConf, SparkContext}

object Q8csv {

  def main(args:Array[String]):Unit={

    /**
     * Create an RDD from a list of strings where each string
     * represents a CSV row. Write a Spark program to parse the rows and filter
     * out records where the age is less than 18.
     */
    val conf=new SparkConf().setAppName("CSV").setMaster("local[*]")
    val sc=new SparkContext(conf)
    val data=Seq("Alice,20,New York","Bob,15,California","Charlie,30,DC","David,10,Seattle","Eve,25,Chicago","Frank,17,LA")
    val rdd=sc.parallelize(data)
    val result=rdd.map(_.split(",")).filter(x=>x(1).toInt>=18).map(x=>(x(0),x(1),x(2))).collect().toList
    println("The adults with age greater than 18 are...")
    println(result)
    sc.close( )

  }

}
