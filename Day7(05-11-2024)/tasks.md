Question 1:Jobrunner  task

Question 2:
Create A function that returns a Future[String]

The function name is RandomNumberThreadExecutor

the function should have a promise created and return associated future

in the function  create three  threads where 
each thread has a logic to generate a random number 

first thread name is firstThread
second thread name is secondThread
third thread name is thirdThread

all three threads runs infinetly

whenever any of the thread gets 1567 as the random number 
it should resolve the promise by using success with
the message threadname+ " has generated " + 1567

it should stop and it should also notify (find a mechanism)
other threads to stop

the main thread should wait for the future to be
complete and print the message