package com.microservice.stockdatastreamer.producer

import org.springframework.stereotype.Component
import com.microservice.stockdatastreamer.common.KafkaConstants
import org.springframework.kafka.core.KafkaTemplate

@Component
class StockDataProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun send(stockMessage: String){
        kafkaTemplate.send(KafkaConstants.INTERNAL_TOPIC, stockMessage)
    }
}