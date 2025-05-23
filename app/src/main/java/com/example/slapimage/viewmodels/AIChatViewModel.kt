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
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/*class AIChatViewModel : ViewModel() {
    private val _chatMessages = MutableStateFlow<List<AIChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<AIChatMessage>> = _chatMessages.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _isModelLoaded = MutableStateFlow(false)
    val isModelLoaded: StateFlow<Boolean> = _isModelLoaded.asStateFlow()

    // Add this convenience property for direct boolean access
    val isModelLoadedDirect: Boolean
        get() = _isModelLoaded.value

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var model: Model? = null
    private var predictor: Predictor<NDList, NDList>? = null
    private var vocabulary: DefaultVocabulary? = null

    //val isModelLoaded: Boolean
      //  get() = model != null && predictor != null

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
                // Update state on main thread
                withContext(Dispatchers.Main) {
                    _isModelLoaded.value = true
                    addChatMessage(AIChatMessage("System", "Model loaded successfully", true))
                }
                //addChatMessage(AIChatMessage("System", "Model loaded successfully", true))
            } catch (e: Exception) {
                closeModel()
                // You might want to update UI with this error
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Error loading model: ${e.localizedMessage}"
                    addChatMessage(AIChatMessage("System", "Error loading model: ${e.localizedMessage}", true))
                }
                //addChatMessage(AIChatMessage("System", "Error loading model: ${e.localizedMessage}", true))
                //throw e
            } finally {
                _isProcessing.value = false
            }
        }
    }
*/
class AIChatViewModel : ViewModel() {
    // Model components
    private var model: Model? = null
    private var vocabulary: DefaultVocabulary? = null
    private var predictor: Predictor<NDList, NDList>? = null

    // Thread management
    private val modelLoaderDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    // State flows
    private val _chatMessages = MutableStateFlow<List<AIChatMessage>>(emptyList())
    private val _isProcessing = MutableStateFlow(false)
    private val _isModelLoaded = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _loadingProgress = MutableStateFlow(0)

    // Public flows
    val chatMessages: StateFlow<List<AIChatMessage>> = _chatMessages.asStateFlow()
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()
    val isModelLoaded: StateFlow<Boolean> = _isModelLoaded.asStateFlow()
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    val loadingProgress: StateFlow<Int> = _loadingProgress.asStateFlow()
    // Add this direct access property
    val isModelLoadedDirect: Boolean
        get() = _isModelLoaded.value

    fun loadModel(context: Context, modelPath: String) {
        if (_isProcessing.value) return

        viewModelScope.launch {
            _isProcessing.value = true
            _errorMessage.value = null
            _loadingProgress.value = 0

            try {
                // Step 1: Prepare vocabulary (10% progress)
                val vocabFile = withContext(Dispatchers.IO) {
                    prepareVocabFile(context)
                }
                _loadingProgress.value = 10

                // Step 2-4: Load model on dedicated thread
                val (loadedModel, loadedVocab, loadedPredictor) = withContext(modelLoaderDispatcher) {
                    // Step 2: Load model (can take time)
                    val model = Model.newInstance("deepseek-r1").apply {
                        load(Paths.get(modelPath))
                    }
                    _loadingProgress.value = 60

                    // Step 3: Prepare vocabulary
                    val vocabulary = DefaultVocabulary.builder()
                        .optMinFrequency(1)
                        .addFromTextFile(vocabFile.toPath())
                        .optUnknownToken("[UNK]")
                        .build()
                    _loadingProgress.value = 80

                    // Step 4: Create predictor
                    val predictor = model.newPredictor(NoopTranslator())
                    _loadingProgress.value = 90

                    Triple(model, vocabulary, predictor)
                }

                // Update state on main thread
                closeModel() // Close any existing model first
                model = loadedModel
                vocabulary = loadedVocab
                predictor = loadedPredictor
                _isModelLoaded.value = true
                _loadingProgress.value = 100
                addChatMessage(AIChatMessage("System", "Model loaded successfully", true))
            } catch (e: Exception) {
                closeModel()
                _errorMessage.value = "Error loading model: ${e.localizedMessage}"
                addChatMessage(AIChatMessage("System", "Error loading model: ${e.localizedMessage}", true))
            } finally {
                _isProcessing.value = false
            }
        }
    }

    private suspend fun prepareVocabFile(context: Context): File = withContext(Dispatchers.IO) {
        val vocabFile = File(context.cacheDir, "vocab.txt")
        if (!vocabFile.exists()) {
            context.assets.open("vocab.txt").use { input ->
                FileOutputStream(vocabFile).use { output ->
                    input.copyTo(output)
                }
            }
        }
        vocabFile
    }

    fun getAIResponse(input: String): String {
        if (!_isModelLoaded.value) throw IllegalStateException("Model not loaded")

        return NDManager.newBaseManager().use { manager ->
            try {
                // Tokenization with proper unknown token handling
                val tokens = input.split(" ").map {
                    vocabulary?.getIndex(it)?.toLong()
                        ?: vocabulary?.getIndex("[UNK]")?.toLong()
                        ?: 0L
                }

                // Create input tensor
                val inputArray = manager.create(tokens.toLongArray())
                    .reshape(Shape(1, tokens.size.toLong()))

                        // Perform inference
                        val output = predictor?.predict(NDList(inputArray))
                    ?: throw IllegalStateException("Predictor not initialized")

                // Process output
                when {
                    output.singletonOrThrow().shape.dimension() == 1 -> {
                        val probs = output.singletonOrThrow().softmax(0)
                        "Prediction: ${probs.argMax().getLong()}"
                    }
                    else -> {
                        val outputIds = output.singletonOrThrow().toLongArray()
                        outputIds.joinToString(" ") { id ->
                            vocabulary?.getToken(id) ?: "[UNK]"
                        }
                    }
                }
            } catch (e: Exception) {
                "Error generating response: ${e.message}"
            }
        }
    }

    fun addChatMessage(message: AIChatMessage) {
        _chatMessages.value = _chatMessages.value + message
    }
    /*
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
*/
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


 //   fun addChatMessage(message: AIChatMessage) {
  //      _chatMessages.value = _chatMessages.value + message
  //  }

    fun setProcessing(processing: Boolean) {
        _isProcessing.value = processing
    }

    //private fun closeModel() {
   //     predictor?.close()
   //     model?.close()
   //     predictor = null
//model = null
   //     vocabulary = null
 //       _isModelLoaded.value = false
  //  }

    private fun closeModel() {
        predictor?.close()
        model?.close()
        predictor = null
        model = null
        vocabulary = null
        _isModelLoaded.value = false
    }

    override fun onCleared() {
        super.onCleared()
        modelLoaderDispatcher.close()
        closeModel()
    }
    //override fun onCleared() {
    //    super.onCleared()
    //    closeModel()
   // }
}