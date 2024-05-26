package com.microservice.stockdatastreamer.service

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class DiscordMessenger {

    private val client = OkHttpClient()

    fun sendToDiscord(webhookUrl: String, message: String): String {
        // Überprüfe, ob die URL und die Nachricht nicht leer sind
        if (webhookUrl.isBlank() || message.isBlank()) {
            return "Webhook URL oder Nachricht darf nicht leer sein."
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonData = """{"content": "$message"}"""
        val body = jsonData.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(webhookUrl)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: "Keine Antwort vom Server"
        }
    }
}

