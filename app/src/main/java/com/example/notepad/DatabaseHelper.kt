package com.example.notepad

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Corrected: All constants defined at the top
private const val DATABASE_NAME = "notepad.db"

private const val DATABASE_VERSION = 1
private const val TABLE_NAME = "notes"
private const val COLUMN_ID = "_id"
private const val COLUMN_NAME = "items"

// Corrected: SQL string with proper spacing
private const val SQL_CREATE_TABLE =
    "CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$COLUMN_NAME TEXT)"


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the old table and create a new one
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Function to insert a new item into the database
    fun insertItem(name: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
        }
        val newRowId = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.d("DatabaseHelper", "Inserted new row with ID: $newRowId")
        return newRowId
    }

    // Function to retrieve all items from the database
    fun getAllItems(): List<String> {
        val itemList = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                itemList.add(name)
            }
        }
        cursor.close()
        db.close()
        return itemList
    }
    //update item

    fun updateItem(orignalName: String, newName: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, newName)
        }
        val updateRows = db.update(
            TABLE_NAME,
            values,
            "$COLUMN_NAME = ?",
            arrayOf(orignalName)
        )
        db.close()
        Log.d("DatabaseHelper", "Deleted $updateRows row(s) for item: $newName")
        return updateRows
    }


    // Add this function to your DatabaseHelper class
    fun deleteItem(name: String): Int {
        val db = writableDatabase
        val deletedRows = db.delete(
            TABLE_NAME,
            "$COLUMN_NAME = ?",
            arrayOf(name)       // This is the value to match
        )
        db.close()
        Log.d("DatabaseHelper", "Deleted $deletedRows row(s) for item: $name")
        return deletedRows
    }
}