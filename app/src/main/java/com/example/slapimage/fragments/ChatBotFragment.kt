package com.example.slapimage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.slapimage.R
//import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ChatBotFragment : Fragment() {
    //private lateinit var interpreter: Interpreter
    private lateinit var inputEditText: EditText
    private lateinit var outputTextView: TextView
    private lateinit var sendButton: Button
    // Declare modelBuffer as a class-level variable
    private lateinit var modelBuffer: MappedByteBuffer
    private val REQUEST_CODE_PERMISSION = 100
    private val REQUEST_CODE_OPEN_FILE = 101



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chatbot, container, false)
        // Load the LFS file and initialize the TensorFlow Lite interpreter
        val modelPath = "/storage/emulated/0/Download/DeepSeek-R1-Distill-Qwen-1.5B.lfs" // Update this path
       // modelBuffer = loadLFSFile(modelPath)
       // interpreter = Interpreter(modelBuffer)

        // Use the interpreter for inference
        // Initialize UI components
        inputEditText = view.findViewById(R.id.inputEditText)
        sendButton = view.findViewById(R.id.sendButton)
        outputTextView = view.findViewById(R.id.outputTextView)

        // Set up the Chat Box
        sendButton.setOnClickListener {
         //   val userInput = inputEditText.text.toString()
         //   val modelOutput = runInference(userInput)
           // outputTextView.text = modelOutput
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun loadLFSFile(filePath: String): MappedByteBuffer {
        val fileInputStream = FileInputStream(filePath)
        val fileChannel = fileInputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size())
    }

    private fun runInference(input: String): String {
        // Preprocess the input (e.g., convert to a float array)
        val inputBuffer = preprocessInput(input)

        // Run inference
        val outputBuffer = FloatArray(1) // Adjust based on your model's output shape
        //interpreter.run(inputBuffer, outputBuffer)

        // Post-process the output (e.g., convert to a string)
        return postprocessOutput(outputBuffer)
    }

    private fun preprocessInput(input: String): FloatArray {
        // Convert the input string to a format suitable for the model
        // Example: Tokenize, normalize, etc.
        return floatArrayOf(1.0f) // Replace with actual preprocessing
    }

    private fun postprocessOutput(output: FloatArray): String {
        // Convert the model's output to a human-readable string
        return "Model output: ${output[0]}" // Replace with actual postprocessing
    }
    override fun onDestroy() {
        super.onDestroy()
//interpreter.close()
    }
}