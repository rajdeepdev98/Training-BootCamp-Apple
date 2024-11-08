/*  */

trait GetStarted {
    def prepare(): Unit = {
        println("GetStarted prepare")
    }
}

trait KeepIngredients extends GetStarted

trait Cook extends GetStarted {
    override def prepare(): Unit = {
        super.prepare()
        println("Cook prepare")
    }
}

trait Seasoning {
    def applySeasoning(): Unit = {
        println("Seasoning applySeasoning")
    }
}

class Food extends Cook with Seasoning {
    def prepareFood(): Unit = {
        prepare()
        applySeasoning()
    }
}

@main def runIt() = {
    println("Starting the main thread....")
    val food = new Food()
    food.prepareFood()
}


//Here  use of abstract override is not required as the method in its superclass is i.e 'GetStarted' is already implemented

/* The output of the above code is:
Starting the main thread....
GetStarted prepare
Cook prepare
Seasoning applySeasoning */

//We will add another example in abstract_override_test.scala file
