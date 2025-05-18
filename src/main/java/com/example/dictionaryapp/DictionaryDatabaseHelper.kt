package com.example.dictionaryapp
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

class DictionaryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "dictionary"
        private const val COLUMN_WORD = "word"
        private const val COLUMN_DEFINITION = "definition"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (id INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_WORD TEXT, $COLUMN_DEFINITION TEXT)"
        db.execSQL(createTable)

        // Thêm một số từ mẫu
        insertWord(db, "verisimilitude", "The appearance of being true or real.")
        insertWord(db, "verify", "To confirm the truth of something.")
    }

    private fun insertWord(db: SQLiteDatabase, word: String, definition: String) {
        val values = ContentValues().apply {
            put(COLUMN_WORD, word)
            put(COLUMN_DEFINITION, definition)
        }
        db.insert(TABLE_NAME, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getDefinition(word: String): String? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_DEFINITION),
            "$COLUMN_WORD = ?",
            arrayOf(word),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val def = cursor.getString(0)
            cursor.close()
            def
        } else {
            cursor.close()
            null
        }
    }

    fun searchWordsContaining(substring: String): List<String> {
        val db = readableDatabase
        val list = mutableListOf<String>()
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_WORD),
            "$COLUMN_WORD LIKE ?",
            arrayOf("%$substring%"),
            null, null, null
        )
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0))
        }
        cursor.close()
        return list
    }
}
