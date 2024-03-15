package com.microservice.stockdatastreamer.producer

import org.springframework.stereotype.Component
import com.microservice.stockdatastreamer.common.KafkaConstants
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.core.KafkaTemplate

@Component
@EnableKafkaStreams
class StockDataProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    fun send(stockMessage: String){
        kafkaTemplate.send(KafkaConstants.INTERNAL_TOPIC, stockMessage)
    }

    fun sendValidData(stockMessage: String){
        kafkaTemplate.send(KafkaConstants.VALID_DATA_TOPIC, stockMessage)
    }
}