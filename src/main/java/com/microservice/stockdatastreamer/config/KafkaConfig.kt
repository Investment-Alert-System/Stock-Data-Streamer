package com.microservice.stockdatastreamer.config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.ExponentialBackOff

@Configuration
open class KafkaConfig {

    @Bean
    open fun defaultErrorHandler() = DefaultErrorHandler(ExponentialBackOff()).apply {
        isAckAfterHandle = false
    }
}
