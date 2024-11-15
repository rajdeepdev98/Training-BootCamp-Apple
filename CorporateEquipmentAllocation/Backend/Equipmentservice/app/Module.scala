import com.google.inject.AbstractModule
import setup.StartupTasks
//import setup.StartupTasks.StartupTasks



class Module extends AbstractModule {
  override def configure(): Unit = {
    // Register services or components explicitly if needed
//    bind(classOf[StartupTasks]).asEagerSingleton()  // Ensures it runs at startup
  }
}