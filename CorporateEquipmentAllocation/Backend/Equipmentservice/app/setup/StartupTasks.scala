package setup

import play.api.inject.ApplicationLifecycle
import service.OverdueReminderService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StartupTasks @Inject() (dbInitializer: DbInitializer,applicationLifecycle: ApplicationLifecycle,overdueReminderService: OverdueReminderService) (implicit ec:ExecutionContext){
//  dbInitializer.createTables()
  println("Starting startup tasks.....")

  overdueReminderService.initialize()


  applicationLifecycle.addStopHook(()=>{
    Future.successful(())
  })


}
