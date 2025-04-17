package com.example.slapimage.gridiconactivity

import android.util.Log
import com.example.slapimage.gridiconactivity.TwelveDataAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class StockRepository  {
    companion object {
        private const val TAG = "StockRepository"
        private const val cacheDurationMinutes = 5L // Follows Kotlin naming conventions

        @Volatile private var instance: StockRepository? = null

        fun getInstance(): StockRepository {
            return instance ?: synchronized(this) {
                instance ?: StockRepository().also { instance = it }
            }
        }
    }

    private val cache = mutableMapOf<String, Pair<StockData, Long>>()

    suspend fun fetchStockData(ticker: String): StockData? {
        return withContext(Dispatchers.IO) {
            getFromCache(ticker) ?: fetchFreshData(ticker)
        }
    }

    private fun getFromCache(ticker: String): StockData? {
        return cache[ticker]?.let { (cachedData, timestamp) ->
            if (System.currentTimeMillis() - timestamp < TimeUnit.MINUTES.toMillis(cacheDurationMinutes)) {
                Log.d(TAG, "Returning cached data for $ticker")
                cachedData
            } else {
                cache.remove(ticker) // Remove expired entry
                null
            }
        }
    }

    private suspend fun fetchFreshData(ticker: String): StockData? {
        return TwelveDataAPI.getStockData(ticker)?.also { newData ->
            Log.d(TAG, "Caching new data for $ticker")
            cache[ticker] = Pair(newData, System.currentTimeMillis())
        }
    }

    fun clearCache() {
        Log.d(TAG, "Clearing entire stock data cache")
        cache.clear()
    }

    fun clearCacheForTicker(ticker: String) {
        Log.d(TAG, "Clearing cache for $ticker")
        cache.remove(ticker)
    }
}