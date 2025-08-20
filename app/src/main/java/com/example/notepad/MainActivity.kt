package com.example.notepad

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

// It's good practice to define Intent keys as constants
const val item_name_key = "item_name"

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var gridView: GridView // Corrected name for clarity
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dataList: MutableList<String>
    // Corrected: Use the proper class name for the FloatingActionButton
    private lateinit var floatButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Removed the extra setContentView() call
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gridView = findViewById(R.id.myGridView)

        floatButton = findViewById(R.id.floatingActionButton)

        databaseHelper = DatabaseHelper(this)

        dataList = databaseHelper.getAllItems().toMutableList()

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            dataList
        )
        gridView.adapter = adapter


        gridView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String

            val intent = Intent(this, SaveEdit::class.java)

            intent.putExtra(ITEM_NAME_KEY, selectedItem)

            startActivity(intent)
        }

        // Set the listener for the Floating Action Button
        floatButton.setOnClickListener {

            val intent = Intent(this, SaveEdit::class.java)
            startActivity(intent)
        }

        // In your onCreate method, replace the setOnItemClickListener with this
        gridView.setOnItemLongClickListener { parent, view, position, id ->
            // Get the item to be deleted
            val selectedItem = parent.getItemAtPosition(position) as String

            databaseHelper.deleteItem(selectedItem)

            dataList.clear()
            val savedItems = databaseHelper.getAllItems()
            dataList.addAll(savedItems)
            adapter.notifyDataSetChanged()

            Toast.makeText(this, "Item Deleted.", Toast.LENGTH_SHORT).show()

            // Return true to indicate the long click event was handled
            true
        }



    }
    // This method is called every time the Activity comes to the foreground
    override fun onResume() {
        super.onResume()

        // Step 1: Clear the current data list to avoid duplicates
        dataList.clear()

        // Step 2: Read all the latest data from the database
        val savedItems = databaseHelper.getAllItems()

        // Step 3: Add the latest data to the list
        dataList.addAll(savedItems)

        // Step 4: Notify the adapter that the data has changed
        adapter.notifyDataSetChanged()
    }
}