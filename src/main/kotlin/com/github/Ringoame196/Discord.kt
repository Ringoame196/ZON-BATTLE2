package com.github.Ringoame196
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class Discord {
    fun sendDiscordWebhookMessage(message: String) {
        try {
            val url = URL(com.github.Ringoame196.data.Data.DataManager.webhook)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")

            val jsonPayload = "{\"content\": \"$message\"}" // メッセージ内容をJSON形式で設定

            val outputStream = OutputStreamWriter(connection.outputStream)
            outputStream.write(jsonPayload)
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == 204) {
                println("メッセージがDiscordに送信されました。")
            } else {
                println("メッセージの送信に失敗しました。レスポンスコード: $responseCode")
            }

            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
