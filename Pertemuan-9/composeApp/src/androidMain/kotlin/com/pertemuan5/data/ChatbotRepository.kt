package com.pertemuan5.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ChatbotRepository {

    suspend fun generateResponse(prompt: String, apiKey: String, model: String): String = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext "Kunci API (API Key) belum dikonfigurasi. Silakan masukkan API Key terlebih dahulu."
        }

        try {
            val cleanModel = model.trim().lowercase().replace(" ", "-")
            val urlString = "https://generativelanguage.googleapis.com/v1beta/models/$cleanModel:generateContent?key=$apiKey"
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.doOutput = true

            // Create JSON Request using Android's built-in JSONObject
            val requestBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
            }

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(requestBody.toString())
                writer.flush()
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                val responseObj = JSONObject(responseText)
                val candidates = responseObj.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "Tidak ada respon.")
                    }
                }
                return@withContext "Format respon dari API tidak sesuai."
            } else {
                val errorText = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                var errorMessage = "Gagal menghubungi API (HTTP $responseCode)."
                if (errorText.isNotBlank()) {
                    try {
                        val errorJson = JSONObject(errorText)
                        val errorObj = errorJson.optJSONObject("error")
                        val errMsg = errorObj?.optString("message")
                        if (!errMsg.isNullOrBlank()) {
                            errorMessage += ": $errMsg"
                        }
                    } catch (e: Exception) {
                        // ignore parsing error
                    }
                }
                return@withContext errorMessage
            }
        } catch (e: Exception) {
            return@withContext "Terjadi kesalahan jaringan atau koneksi: ${e.localizedMessage ?: "Unknown error"}"
        }
    }
}
