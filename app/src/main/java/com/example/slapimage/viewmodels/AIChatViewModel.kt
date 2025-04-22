package com.example.slapimage.viewmodels

import ai.djl.Model
import ai.djl.inference.Predictor
import ai.djl.modality.nlp.DefaultVocabulary
import ai.djl.ndarray.NDList
import ai.djl.ndarray.NDManager
import ai.djl.ndarray.types.Shape
import ai.djl.translate.NoopTranslator
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.slapimage.models.AIChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Paths
import kotlinx.coroutines.Dispatchers

class AIChatViewModel : ViewModel() {
    private val _chatMessages = MutableStateFlow<List<AIChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<AIChatMessage>> = _chatMessages.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private var model: Model? = null
    private var predictor: Predictor<NDList, NDList>? = null
    private var vocabulary: DefaultVocabulary? = null

    val isModelLoaded: Boolean
        get() = model != null && predictor != null

    fun loadModel(context: Context, modelPath: String) {
        //fun loadModel(modelPath: String) {
        viewModelScope.launch {
            try {
                _isProcessing.value = true
                closeModel()

                // First ensure vocab file exists in cache
                val vocabFile = prepareVocabFile(context)
                // Load the model
                model = Model.newInstance("deepseek-r1").apply {
                    load(Paths.get(modelPath))
                }
                // Load vocabulary from the prepared file
                vocabulary = DefaultVocabulary.builder()
                    .optMinFrequency(1)
                    .addFromTextFile(vocabFile.toPath())
                    .optUnknownToken("[UNK]")
                    .build()

                predictor = model?.newPredictor(NoopTranslator())
                // Add system message confirming model loaded
                addChatMessage(AIChatMessage("System", "Model loaded successfully", true))
            } catch (e: Exception) {
                closeModel()
                // You might want to update UI with this error
                addChatMessage(AIChatMessage("System", "Error loading model: ${e.localizedMessage}", true))
                //throw e
            } finally {
                _isProcessing.value = false
            }
        }
    }

    private suspend fun prepareVocabFile(context: Context): File = withContext(Dispatchers.IO) {
        val vocabFile = File(context.cacheDir, "vocab.txt")
        if (!vocabFile.exists()) {
            try {
                context.assets.open("vocab.txt").use { input ->
                    FileOutputStream(vocabFile).use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                throw IllegalStateException("Failed to prepare vocabulary file", e)
            }
        }
        return@withContext vocabFile
    }

    /*
    fun getAIResponse(input: String): String {
        if (!isModelLoaded) throw IllegalStateException("Model not loaded")

        return NDManager.newBaseManager().use { manager ->
            try {
                // Simple tokenization using vocabulary directly
                val tokens = input.split(" ")
                val inputIds = LongArray(tokens.size) { index ->
                    vocabulary?.getIndex(tokens[index])?.toLong()
                        ?: vocabulary?.getIndex("[UNK]")?.toLong()
                        ?: throw IllegalStateException("Vocabulary not initialized")
                }

                // Create input tensor [1, sequence_length]
                val inputArray = manager.create(inputIds).reshape(Shape(1, inputIds.size.toLong()))
                val output = predictor?.predict(NDList(inputArray))
                    ?: throw IllegalStateException("Predictor not initialized")

                output.singletonOrThrow().toString()
            } catch (e: Exception) {
                "Error generating response: ${e.message}"
            }
        }
    }
*/
// In AIChatViewModel.kt
fun getAIResponse(input: String): String {
    if (!isModelLoaded) throw IllegalStateException("Model not loaded")

    return NDManager.newBaseManager().use { manager ->
        try {
            // 1. Tokenization with proper unknown token handling
            val tokens = input.split(" ").map {
                vocabulary?.getIndex(it)?.toLong()
                    ?: vocabulary?.getIndex("[UNK]")?.toLong()
                    ?: 0L
            }

            // 2. Create input tensor with explicit type and shape toLong
            val inputArray = manager.create(tokens.toLongArray())
                .reshape(Shape(1, tokens.size.toLong()))


            // 3. Perform inference
            val output = predictor?.predict(NDList(inputArray))
                ?: throw IllegalStateException("Predictor not initialized")


            // 4. Proper output processing
            return@use when {
                // For classification models
                output.singletonOrThrow().shape.dimension() == 1 -> {
                    val probs = output.singletonOrThrow().softmax(0)
                    "Prediction: ${probs.argMax().getLong()}"
                }
                // For text generation
                else -> {
                    val outputIds = output.singletonOrThrow().toLongArray()
                    outputIds.joinToString(" ") { id ->
                        vocabulary?.getToken(id) ?: "[UNK]"
                    }
                }
            }
        } catch (e: Exception) {
            "Error generating response: ${e.message}\n${e.stackTraceToString()}"
        }
    }
}

    fun addChatMessage(message: AIChatMessage) {
        _chatMessages.value = _chatMessages.value + message
    }

    fun setProcessing(processing: Boolean) {
        _isProcessing.value = processing
    }

    private fun closeModel() {
        predictor?.close()
        model?.close()
        predictor = null
        model = null
        vocabulary = null
    }

    override fun onCleared() {
        super.onCleared()
        closeModel()
    }
}