package com.microservice.stockdatastreamer.config
import lombok.Value
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.ExponentialBackOff

@Configuration
open class KafkaConfig{

    private var bootstrapServers = "localhost:9092"

    @Bean
    open fun defaultErrorHandler() = DefaultErrorHandler(ExponentialBackOff()).apply {
        isAckAfterHandle = false
    }

    @Bean
    open fun producerFactory(): ProducerFactory<String, String> {
        val configProps= hashMapOf<String, Any>(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    open fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }


}
