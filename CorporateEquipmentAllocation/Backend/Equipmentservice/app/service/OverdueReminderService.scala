package service


import model.{Equipment, EquipmentAllocation, MessageSchema}
import org.apache.pekko.actor.{ActorSystem, Cancellable}
import play.api.libs.json.Json
import repository.EquipmentAllocationRepository
import utils.Constants._
import utils.MyImplicitConversions._

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@Singleton
class OverdueReminderService @Inject()(equipmentAllocationRepository: EquipmentAllocationRepository, kafkaProducerService: KafkaProducerService)(implicit ec: ExecutionContext, actorSystem: ActorSystem) {

  println("OverdueReminderService started...........every 30 seconds.")

  private val scheduler: Cancellable = actorSystem.scheduler.scheduleAtFixedRate(initialDelay = 0.seconds, interval = 1.day) { () =>
    sendOverdueReminders()
  }

  def initialize(): Unit = {
    println("Initializing overdue reminders")
  }
  private def sendOverdueReminders(): Unit = {

    println("Sending overdue reminders...........againn..")
    //    val now = LocalDateTime.now()
    try {
      equipmentAllocationRepository.findOverdueAllocations().map(results => {
        results.foreach {
          case (allocation: EquipmentAllocation, equipment: Equipment) => {
            {
              println(allocation)
              val kafkaMessage: MessageSchema = (allocation, equipment, REMINDER)
              kafkaProducerService.sendMessage( "key", Json.toJson(kafkaMessage).toString())
            }
          }
        }
      })
    }catch
    {
      case e:Exception=>println(e.getMessage)
    }

  }

}
