import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions
import scala.collection.mutable.Queue
 import scala.io.StdIn

object Program{

    case class Result(success:Boolean=true,message:String="success")
    case class Employee(
        sno:Int,
        name:String,
        city:String,
        department:String
    )
    //companion object to work as Factory for Employee Creation
    object Employee{

        private var count:Int=1
        def apply(name:String,city:String,department:String):Employee={

            val emp:Employee=new Employee(count,name,city,department)
            count+=1
            emp// returning the employee after creation
        }
    }
    //Implicit conversion of Tuple to Employee
    implicit def tupleToEmployee(t:(String,String,String)):Employee={
        Employee(t._1,t._2,t._3)
    }
    // val utilFn:(Int)=>Unit=(num)=>{
    //     for(i<-0 until num)
    //     {
    //         print("   ")
    //     }
    //     // if(num>=0){
    //     //     print("|--")
    //     // }
    // }


    //Class Tree contains an organization structure->having list of Employees as well as List of Departments[Other nodes]
    class Tree(val department:String="ORGANIZATION"){
        
        val employees:ListBuffer[Employee]=ListBuffer[Employee]() // list of employees under the current department
        val departments:ListBuffer[Tree]=ListBuffer[Tree]()//List of Departments under the current department

        //Method to add an employee to a department
        def addEmployee(emp:Employee):Result={
            val deptName:String=emp.department
            val department:Option[Tree]=this.findDepartment(deptName)
            if(department.isEmpty){
                return Result(false,"Department does not exist")
            }
            else{
                department.get.employees+=emp
            }

            Result(message="Successfully added Employee")
        }
        def addDepartment(parentDept:String,department:String):Result={
              /*   for adding department to a particular department->first check department exists->
              if yes->return false t
              hen check if parent exists 
              if not -> return false
            else add the department to the parent department
                */
            val dept:Option[Tree]=this.findDepartment(department)
            if(dept.isDefined){
                return Result(false,"Department already exists")
            }
            val parent:Option[Tree]=this.findDepartment(parentDept)
            if(parent.isEmpty){
                return Result(false,"Parent Department does not exist")
            }
            val newDept:Tree=new Tree(department)
            parent.get.departments+=newDept
            Result(message="Successfully added Department")

        } 

        def findDepartment(department:String):Option[Tree]={
            if(this.department.equalsIgnoreCase(department)){
                return Some(this)
            }
            for(dept<-departments){
                val res:Option[Tree]=dept.findDepartment(department)
                if(res.isDefined){
                    return res
                }
            }
            None
        }

        // def printStructure():Unit={
        //     printStructure(0)
        // }
        // def printStructure(level:Int):Unit={
            
        //     utilFn(level-1)
        //     if(level>=1)print("|--")
        //     println(s"$department")
        //     for(dept<-departments){

        //         dept.printStructure(level+1)
        //     }
        //     // println("Employees : ")
        //     for(emp<-employees){
        //         utilFn(level)
        //         // print("|  ")
        //         println(s"|--$emp")
        //     }
           
            
        // }
        def printStructure(indent: String = "", prefix: String = "",next:Boolean=false): Unit = {
            // Print the current employee with their title
            println(s"$indent$prefix$department")
            
            // For each subordinate, recursively print with increased indentation and appropriate prefixes
            employees.zipWithIndex.foreach {
            case (employee, idx) =>
                val empPrefix ="|--"
                val nextIndent=if(next)"|   "else "    "
                val empIndent=s"$indent$nextIndent"//4 spaces
                println(s"$empIndent$empPrefix$employee")
                // employee.printHierarchy(indent + (if (idx == employees.size - 1) "    " else "│   "), newPrefix)
            }
            departments.zipWithIndex.foreach {
                case (department, idx) =>
                    val newPrefix = if (idx == departments.size - 1) "└── " else "├── "
                    val childNext=(idx!=(departments.size-1))
                    department.printStructure(indent + (if(next)"|   " else "    "), newPrefix,childNext)
            }
        }

    }
    

 
   

        
    def main(args:Array[String]):Unit={
        // println("Organization structure as a Tree...")

        val apple:Tree=new Tree()// Root Node <--> ORGANIZATION node

        //Adding Departments to the Organization ->in this case replicating the organization structure as given in the problem statement
        apple.addDepartment("ORGANIZATION","Finance")
        apple.addDepartment("Finance","Payments")
        apple.addDepartment("Finance","Sales")
        apple.addDepartment("Sales","Marketing")
        apple.addDepartment("Sales","Advertisements")
        apple.addDepartment("Sales","SalesManagement")

        //adding employees to the departments

        apple.addEmployee(("Ravi","Chennai","Payments"))
        apple.addEmployee(("Ram","Chennai","Payments"))
        apple.addEmployee(("Rohan","Kolkata","Marketing"))
        apple.addEmployee(("Rakesh","Mumbai","Marketing"))
        apple.addEmployee(("Ravi","Mumbai","Sales"))
        apple.addEmployee(("Ricky","Chennai","Advertisements"))

        //Added some departments and employees above to create some structure
        // Now lets loop to show the menu to the user    
        var check:Boolean=true
        while(check){
            println("\nEnter your choice:")
            println("1. Add Department")
            println("2. Add Employee to a Department")
            println("3. Show Organization Tree")
            println("4. Exit")

      // Read the user choice
   
            val choice = StdIn.readLine()

            choice match {
                case "1" =>{
                    // Add a new department
                    println("Enter Parent Department Name: ")
                    val parentdeptName = StdIn.readLine()
                    println("Enter Department Name: ")
                    val department=StdIn.readLine()
                    val result:Result=apple.addDepartment(parentdeptName,department)
                    println(result.message)
                }
                case "2"=>{
                    // Add a new employee
                    println("Enter Employee Name: ")
                    val name = StdIn.readLine()
                    println("Enter Employee City: ")
                    val city = StdIn.readLine()
                    println("Enter Employee Department: ")
                    val department = StdIn.readLine()
                    val result:Result=apple.addEmployee((name,city,department))
                    println(result.message)

                }
                case "3"=>{
                    // Show the organization structure
                    apple.printStructure()
                }
                case "4"=>{
                    // Exit the program
                    check=false
                    println("Exiting the program...")

                }
                case _ =>{
                    println("Invalid Choice...Try again")
                }
                }



         }
    }
}



