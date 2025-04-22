package com.example.slapimage.gridiconactivity

import android.util.Log
import com.example.slapimage.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object TwelveDataAPI {
    private const val TAG = "TwelveDataAPI"
    private const val BASE_URL = "https://api.twelvedata.com"
    private const val API_KEY = BuildConfig.TWELVEDATA_API_KEY // Replace with your key

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // Removed suspend modifier - this is now a regular blocking function
    fun getStockData(ticker: String): StockData? {
        return try {
            val url = "$BASE_URL/quote?symbol=$ticker&apikey=$API_KEY"
            Log.d(TAG, "Requesting: $url")

            val request = Request.Builder()
                .url(url)
                .addHeader("User-Agent", "YourApp/1.0")
                .build()

            val response = client.newCall(request).execute()
            val jsonData = response.body?.string() ?: return null

            parseResponse(jsonData, ticker)
        } catch (e: Exception) {
            Log.e(TAG, "API call failed", e)
            null
        }
    }

    private fun parseResponse(jsonData: String, ticker: String): StockData? {
        return try {
            val json = JSONObject(jsonData)

            if (json.has("code")) {
                Log.w(TAG, "API Error: ${json.optString("message")}")
                return null
            }

            StockData(
                symbol = json.optString("symbol", ticker),
                price = json.optDouble("close", 0.0).toFloat(),
                open = json.optDouble("open", 0.0).toFloat(),
                high = json.optDouble("high", 0.0).toFloat(),
                low = json.optDouble("low", 0.0).toFloat(),
                volume = json.optLong("volume", 0),
                latestTradingDay = json.optString("datetime", "N/A"),
                previousClose = json.optDouble("previous_close", 0.0).toFloat(),
                change = json.optDouble("change", 0.0).toFloat(),
                changePercent = json.optString("percent_change", "0%")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Parsing failed", e)
            null
        }
    }
}