object MainProg{

    trait Athlete{
        def playSport():List[String]
    }
    trait CricketPlayer extends Athlete{
         abstract override def playSport():List[String]= super.playSport() :+ "Cricket"
    }
    trait FootballPlayer extends Athlete{
          abstract override def playSport():List[String] = super.playSport():+"Football"
    }
   
    class Beginner extends Athlete{
         override def playSport():List[String] = List.empty[String]
    }
    def main(args: Array[String]): Unit = {
        
       val v1=new Beginner()
       println(v1.playSport())//prints an empty list

       val v2=new Beginner with CricketPlayer with FootballPlayer ()
       println(v2.playSport())//prints List(Cricket)

       //In this example , abstract override is necessary as the method in its superclass is not implemented and yet it is mixed in

    }
}
