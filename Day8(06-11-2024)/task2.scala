trait Task {
    def doTask(): Unit = println("Doing task")
}
trait Cook extends Task {
    // def cookFood(): Unit = println("Cooking food")
    override def doTask():Unit={
        println("Cook is cooking food")
    }
}

trait Garnish extends Cook {

    override def doTask():Unit={
        println("Garnishing food")

    }
}

trait Pack extends Garnish {

    override def doTask():Unit={
        println("Packing food")

    }
    
}
class Activity extends Task{
    def doActivity():Unit={
        println("Doing activity")

        super.doTask()
    }
}

@main def execute():Unit={

    println("Main program starts here....")
    /*  For Linearization,scala uses a Linearized Graph(Singly Linked List)that it creates from the Inheritance tree->It does a dfs for each
        of the traits/classes from left to right,and makes sure that anything already added is not added again in the linearized graph
    */
    

    //Linearization Order in this case is Activity -> Pack -> Garnish -> Cook -> Task
    val task:Task=new Activity with Cook with Garnish with Pack
    task.doTask() //this takes the last implementation of doTask method ->prints 'Packing food'
    
    //Linearization Order in this case is Activity->Pack->Garnish->Cook->Task
    val task2:Task=new Activity with Garnish with Pack with Cook
    task2.doTask() //this takes the last implementation of doTask method ->prints 'Packing food'

    //Linearization Order in this case is Activity->Pack->Garnish->Cook->Task
    val task3:Task=new Activity with Pack with Garnish with Cook
    task3.doTask() //this takes the last implementation of doTask method ->prints 'Packing food'
    
    
}   
