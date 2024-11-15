package service

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import play.api.Configuration

import java.util.Properties
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import org.apache.kafka.clients.producer.RecordMetadata

@Singleton
class KafkaProducerService @Inject()(config:Configuration)(implicit ec:ExecutionContext) {

  // Kafka producer configuration
  private val kafkaConfig = config.get[Configuration]("kafka")

  // Kafka producer properties
  private val producerProps = new Properties()
  producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.get[String]("bootstrapServers"))
  producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaConfig.get[String]("clientId"))
  producerProps.put(ProducerConfig.ACKS_CONFIG, kafkaConfig.get[String]("acks"))
  producerProps.put(ProducerConfig.RETRIES_CONFIG, kafkaConfig.get[Int]("retries").toString)
  producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)


  private val kafkaProducer=new KafkaProducer[String,String](producerProps)

  def sendMessage(topic: String, key: String, value: String): Unit = {
    val record = new ProducerRecord[String, String](topic, key, value)
    kafkaProducer.send(record, (metadata: RecordMetadata, exception: Exception) => {
      if (exception != null) {
        exception.printStackTrace()
      } else {
        println(s"Record sent to partition ${metadata.partition()}, offset ${metadata.offset()}")
      }
    })

  }

  // Close the producer gracefully
  def close(): Unit = {
    kafkaProducer.close()
  }

}
