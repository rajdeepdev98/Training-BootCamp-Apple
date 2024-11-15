package setup

import play.api.inject.ApplicationLifecycle

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class StartupTasks @Inject() (dbInitializer: DbInitializer,applicationLifecycle: ApplicationLifecycle) {
  dbInitializer.createTables()

  applicationLifecycle.addStopHook(()=>{
    Future.successful(())
  })


}
