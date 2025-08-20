package com.example.notepad

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Use the same key constant as in MainActivity
const val ITEM_NAME_KEY = "item_name"

class SaveEdit : AppCompatActivity() {

    private lateinit var itemData: EditText
    private lateinit var backBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var textViewUpdate: TextView
    // Declare a DatabaseHelper instance
    private lateinit var databaseHelper: DatabaseHelper

    private var originalItemName: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_save_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.saveEdit)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        itemData = findViewById(R.id.editTextMlItem)
        backBtn = findViewById(R.id.buttonBack)
        saveBtn = findViewById(R.id.buttonSave)
        textViewUpdate = findViewById(R.id.textViewSaveUpdate)

        // Initialize the database helper
        databaseHelper = DatabaseHelper(this)

        // Check if data was passed from MainActivity
        val receivedData = intent.getStringExtra(ITEM_NAME_KEY)
        if (receivedData != null) {
            originalItemName=receivedData
            saveBtn.text="Update"
            textViewUpdate.text="Update"
            // If data exists, pre-fill the EditText
            itemData.setText(receivedData)
        }

        // Action for the Back button
        backBtn.setOnClickListener {
            finish()
        }

        // Action for the Save button
        saveBtn.setOnClickListener {
            // Get the text from the EditText
            val newItemText = itemData.text.toString().trim()

            if (newItemText.isNotEmpty()) {

                if (originalItemName != null) {

                    databaseHelper.updateItem(originalItemName!!,newItemText)
                    Toast.makeText(this, "Item Update successfully!", Toast.LENGTH_SHORT).show()

                    // Use finish() to return to MainActivity
                    finish()

                }else{
                        // Insert the new item into the database
                        databaseHelper.insertItem(newItemText)

                        Toast.makeText(this, "Item saved successfully!", Toast.LENGTH_SHORT).show()

                        // Use finish() to return to MainActivity
                        finish()
                }


            } else {
                Toast.makeText(this, "Please enter some text to save.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}