package com.example.slapimage.gridiconactivity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.slapimage.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

class StockActivity : AppCompatActivity() {

    // Views
    private lateinit var etTicker: TextInputEditText
    private lateinit var btnFetch: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var tvPrice: TextView
    private lateinit var tvOpen: TextView
    private lateinit var tvHigh: TextView
    private lateinit var tvLow: TextView
    private lateinit var tvVolume: TextView
    private lateinit var tvChange: TextView

    // Coroutine
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val ioDispatcher = Dispatchers.IO

    // API Client
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    companion object {
        private const val TAG = "StockActivity"
        private const val TWELVE_DATA_API_KEY = "c480aa1660db419594c2d89af02dbd1f" // Replace with your API key
        private const val BASE_URL = "https://api.twelvedata.com"
    }
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lock screen orientation to portrait
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_stock)
        initializeViews()
        setupToolbar()
        setupButton()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.stock)
        }
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initializeViews() {
        etTicker = findViewById(R.id.etTicker)
        btnFetch = findViewById(R.id.btnFetch)
        progressBar = findViewById(R.id.progressBar)
        tvPrice = findViewById(R.id.tvPrice)
        tvOpen = findViewById(R.id.tvOpen)
        tvHigh = findViewById(R.id.tvHigh)
        tvLow = findViewById(R.id.tvLow)
        tvVolume = findViewById(R.id.tvVolume)
        tvChange = findViewById(R.id.tvChange)
    }

    private fun setupButton() {
        btnFetch.setOnClickListener {
            val ticker = etTicker.text.toString().trim()
            if (ticker.isNotEmpty()) {
                fetchStockData(ticker)
            } else {
                showSnackbar(getString(R.string.error_empty_ticker))
            }
        }
    }

    private fun fetchStockData(ticker: String) {
        showLoading(true)
        coroutineScope.launch {
            try {
                val stockData = withContext(ioDispatcher) {
                    getStockDataFromAPI(ticker) // Not suspend anymore
                }
                stockData?.let {
                    displayStockData(it)
                } ?: showSnackbar(getString(R.string.error_data_fetch))
            } catch (e: Exception) {
                Log.e(TAG, "Fetch error", e)
                showSnackbar(getString(R.string.error_network, e.localizedMessage ?: getString(R.string.error_unknown)))
            } finally {
                showLoading(false)
            }
        }
    }


    // Removed suspend modifier since it doesn't call other suspend functions
    private fun getStockDataFromAPI(ticker: String): StockData? {
        return try {
            val url = "$BASE_URL/quote?symbol=$ticker&apikey=$TWELVE_DATA_API_KEY"
            val request = Request.Builder()
                .url(url)
                .addHeader("User-Agent", "StockApp/1.0")
                .build()

            val response = client.newCall(request).execute()
            val jsonData = response.body?.string() ?: return null

            parseStockData(jsonData, ticker)
        } catch (e: Exception) {
            Log.e(TAG, "API call failed", e)
            null
        }
    }

    private fun parseStockData(jsonData: String, ticker: String): StockData? {
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
                latestTradingDay = json.optString("datetime", getString(R.string.label_na)),
                previousClose = json.optDouble("previous_close", 0.0).toFloat(),
                change = json.optDouble("change", 0.0).toFloat(),
                changePercent = json.optString("percent_change", "0%")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Parsing failed", e)
            null
        }
    }

    private fun displayStockData(stockData: StockData) {
        tvPrice.text = getString(R.string.price_label, formatCurrency(stockData.price))
        tvOpen.text = getString(R.string.open_label, formatCurrency(stockData.open))
        tvHigh.text = getString(R.string.high_label, formatCurrency(stockData.high))
        tvLow.text = getString(R.string.low_label, formatCurrency(stockData.low))
        tvVolume.text = getString(R.string.volume_label, formatNumber(stockData.volume))
        tvChange.text = getString(
            R.string.change_label,
            formatCurrency(stockData.change),
            stockData.changePercent
        )

        val colorRes = if (stockData.change >= 0) R.color.green_up else R.color.red_down
        tvChange.setTextColor(ContextCompat.getColor(this, colorRes))
    }

    // this will display in local currency and not USD
    // private fun formatCurrency(value: Float): String {
    //    return NumberFormat.getCurrencyInstance().format(value.toDouble())
    // }

    // Forced to display only in USD
    private fun formatCurrency(value: Float): String {
        val usdFormat = NumberFormat.getCurrencyInstance().apply {
            // Force USD currency format
            maximumFractionDigits = 2
            minimumFractionDigits = 2
            currency = java.util.Currency.getInstance("USD")
        }
        return usdFormat.format(value.toDouble())
    }

    private fun formatNumber(value: Long): String {
        return NumberFormat.getInstance().format(value)
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnFetch.isEnabled = !show
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    //override fun onDestroy() {
    //    super.onDestroy()
    //    coroutineScope.cancel()
    //}
    private fun clearStockDataResources() {
        // Release any chart views/data visualizations
        tvPrice.text = ""
        tvOpen.text = ""
        // ... clear other TextViews

        // Cancel any pending requests
        coroutineScope.cancel()

        // Clear image resources if used
        window.decorView.background = null
    }

    override fun onDestroy() {
        clearStockDataResources()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        clearStockDataResources()
       // supportFinishAfterTransition()
        // Mimic back press (NavController handles fragment state)
        onBackPressedDispatcher.onBackPressed() // This will trigger our callback
        return true
    }
}

data class StockData(
    val symbol: String,
    val price: Float,
    val open: Float,
    val high: Float,
    val low: Float,
    val volume: Long,
    val latestTradingDay: String,
    val previousClose: Float,
    val change: Float,
    val changePercent: String,
    val dataSource: String = "Unknown" // Added this field
)