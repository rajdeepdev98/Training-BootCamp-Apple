import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import setup.StartupTasks



//This is the module that binds the StartupTasks class as an eager singleton
class MyModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[StartupTasks]).asEagerSingleton() //schedules the overdue reminder service
    println("Binding Modules...")
  }
}
