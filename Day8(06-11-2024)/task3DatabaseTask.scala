import java.sql.{Connection, DriverManager,Statement, ResultSet}
import scala.language.implicitConversions

case class Candidate(sno:Int,name:String,city:String)
implicit def tupleToCandidate(t:(Int,String,String)):Candidate={
    Candidate(t._1,t._2,t._3)
}

val DB_URL:String="jdbc:mysql://scaladb.mysql.database.azure.com:3306/rajdeep_scala"
val DB_USER:String = "mysqladmin"
val DB_PASSWORD:String = "Password@12345"
//Need to initialize a Database now
class DBConnection{

    var connection:Option[Connection]=None
    var statement:Option[Statement]=None
    def connect():Unit={

        try{
            // Class.forName("com.mysql.cj.jdbc.Driver")
            val con:Connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD)
            connection=Some(con)
            // statement=Some(connection.get.createStatement())
            statement=Some(con.createStatement())

            println("Connection established successfully....Creating table now....")
        }
        catch{
            case e:Exception=>println(e)

        }

    }
    def disconnect():Unit={
        try{
            if(connection.isDefined)connection.get.close()
            println("Connection closed successfully")
        }
        catch{
            case e:Exception=>println(e)
        }
    }
    def createTable():Unit={
        try{
            if(statement.isDefined){
                val createTableSQL:String="""CREATE TABLE IF NOT EXISTS candidates(
                    sno INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100),
                    city VARCHAR(100)
                    )
                """
                println(s"Executing the query: $createTableSQL")
                statement.get.execute(createTableSQL)
                println("Table created successfully")
            }
        }
        catch{
            case e:Exception=>println(e)
        }
    }
    def insertCandidate(candidate:Candidate):Unit={
        try{
            if(statement.isDefined){
                val insertSQL:String=s"INSERT INTO candidates (name, city) VALUES ('${candidate.name}', '${candidate.city}')"
                statement.get.executeUpdate(insertSQL)
                println("Data inserted successfully")
            }
        }
        catch{
            case e:Exception=>println(e)
        }
    }
    def selectQuery():Unit={
        try{
            if(statement.isDefined){

                println("Getting the data from the table.....")
              
                val query="SELECT * from candidates"
                val resultSet:ResultSet=statement.get.executeQuery(query)
                while(resultSet.next()){
                    val sno=resultSet.getInt("sno")
                    val name=resultSet.getString("name")
                    val city=resultSet.getString("city")
                    println(s"sno $sno, name: $name, city: $city")
                }
            }
        }
        catch{
            case e:Exception=>println(e)
        }
    }

}

object MainApp{

    def main(args: Array[String]): Unit = {
        
        println("Starting the program...")
        
        //data to be entered

        val candidateData: Array[(Int, String, String)] = Array(
            (1, "Alice", "New York"),
            (2, "Bob", "Los Angeles"),
            (3, "Charlie", "Chicago"),
            (4, "Diana", "Houston"),
            (5, "Eve", "Phoenix"),
            (6, "Frank", "Philadelphia"),
            (7, "Grace", "San Antonio"),
            (8, "Hank", "San Diego"),
            (9, "Ivy", "Dallas"),
            (10, "Jack", "San Jose"),
            (11, "Kathy", "Austin"),
            (12, "Leo", "Jacksonville"),
            (13, "Mona", "Fort Worth"),
            (14, "Nina", "Columbus"),
            (15, "Oscar", "Charlotte"),
            (16, "Paul", "San Francisco"),
            (17, "Quinn", "Indianapolis"),
            (18, "Rita", "Seattle"),
            (19, "Steve", "Denver"),
            (20, "Tina", "Washington"),
            (21, "Uma", "Boston"),
            (22, "Vince", "El Paso"),
            (23, "Wendy", "Detroit"),
            (24, "Xander", "Nashville"),
            (25, "Yara", "Portland"),
            (26, "Zane", "Oklahoma City"),
            (27, "Aiden", "Las Vegas"),
            (28, "Bella", "Louisville"),
            (29, "Caleb", "Baltimore"),
            (30, "Daisy", "Milwaukee"),
            (31, "Ethan", "Albuquerque"),
            (32, "Fiona", "Tucson"),
            (33, "George", "Fresno"),
            (34, "Hazel", "Mesa"),
            (35, "Ian", "Sacramento"),
            (36, "Jill", "Atlanta"),
            (37, "Kyle", "Kansas City"),
            (38, "Luna", "Colorado Springs"),
            (39, "Mason", "Miami"),
            (40, "Nora", "Raleigh"),
            (41, "Owen", "Omaha"),
            (42, "Piper", "Long Beach"),
            (43, "Quincy", "Virginia Beach"),
            (44, "Ruby", "Oakland"),
            (45, "Sam", "Minneapolis"),
            (46, "Tara", "Tulsa"),
            (47, "Ursula", "Arlington"),
            (48, "Victor", "New Orleans"),
            (49, "Wade", "Wichita"),
            (50, "Xena", "Cleveland")
            )
            
            val db=new DBConnection()
            db.connect()
            db.createTable()

            //Inserting candidates into the database
            for(candidate<-candidateData){
                db.insertCandidate(candidate)
            }

            //Selecting the data from the database
            db.selectQuery()

            //closing the connection
            db.disconnect()



    }
}