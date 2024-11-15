package setup

import com.google.inject.AbstractModule
import model.{EquipmentAllocationTable, EquipmentTable}
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Singleton
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.inject.ApplicationLifecycle
//import slick.lifted
import slick.jdbc.JdbcProfile

import javax.inject.Inject

@Singleton
class DbInitializer @Inject()(dbConfigProvider:DatabaseConfigProvider)(implicit ec:ExecutionContext){
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._


  val equipments=TableQuery[EquipmentTable]
  val equipmentAllocations=TableQuery[EquipmentAllocationTable]

  val schemas=equipments.schema++equipmentAllocations.schema



  def createTables()= {

    db.run(schemas.createIfNotExists).map(_ => println("Tables created")).recover({
      case ex: Exception => println(s"Error creating tables: ${ex.getMessage}")
    })
  }
//  lifecycle.addStopHook { () =>
//    println("lokokokokokoookok")
//    Future.successful(())
//  }



  createTables()

//    applicationLifecycle.addStopHook { () =>
//      db.run(schemas.dropIfExists).map(_=>println("Tables dropped")).recover({
//        case ex:Exception=>println(s"Error dropping tables: ${ex.getMessage}")
//      })

//    }
//  lifecycle.addStopHook(
//    Future.successful(())
//  )








}


