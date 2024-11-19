import scala.util.Random
import scala.util.Try
import scala.concurrent.{Future, Promise,Await}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration.Duration.Inf

    //like java->a class/object with a main function

def test():Future[String]={

        val promise:Promise[String]=Promise[String]()
        val future=promise.future

        def randomFunc():Unit={
            val random = new Random()
            while(!promise.future.isCompleted){
                
                    val randomIntInRange = random.nextInt(10000) // Random int between 0 and 99
                    // println(s"Thread ${Thread.currentThread().getName()} generated: $randomIntInRange")
                    // Thread.sleep(500)
                    if(randomIntInRange==1567){
                        promise.tryComplete(Try(s"Thread ${Thread.currentThread().getName()} Found 1567"))
                        return
                    }

             } 
        }

        val thread1:Thread = new Thread(new Runnable {
            def run(): Unit = {
            randomFunc()
    
            }
      })
        val thread2:Thread = new Thread(new Runnable {
            def run(): Unit = {
                randomFunc()
            }
        })
        val thread3:Thread = new Thread(new Runnable {
            val random = new Random()
            def run(): Unit = {
                randomFunc()
            }
            })

        thread1.setName("Thread1")
        thread2.setName("Thread2")
        thread3.setName("Thread3")

        // val future:Future[String]=promise.future

        thread1.start()
        thread2.start()
        thread3.start()


        future
        

 }


@main def runIt()={
    println("Staring the main thread....")
    
    val future:Future[String]=test()

    future.onComplete{
        case scala.util.Success(value) =>{ println(value)}
        case scala.util.Failure(exception) => println(exception)
    }

    //  var restult:String=Await.result(future,Inf)

    println("Ending the main thread....")


}