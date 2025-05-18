package com.example.dictionaryapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.example.dictionaryapp.DictionaryDatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DictionaryDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wordInput = findViewById<EditText>(R.id.wordInput)
        val lookupButton = findViewById<Button>(R.id.lookupButton)
        val definitionView = findViewById<TextView>(R.id.definitionView)
        val suggestionsView = findViewById<TextView>(R.id.suggestionsView)

        dbHelper = DictionaryDatabaseHelper(this)

        lookupButton.setOnClickListener {
            val word = wordInput.text.toString().trim().lowercase()

            val definition = dbHelper.getDefinition(word)
            if (definition != null) {
                definitionView.text = "Definition: $definition"
                suggestionsView.text = ""
            } else {
                definitionView.text = "No exact match found."
                val suggestions = dbHelper.searchWordsContaining(word)
                suggestionsView.text = "Suggestions:\n" + suggestions.joinToString("\n")
            }
        }
    }
}
