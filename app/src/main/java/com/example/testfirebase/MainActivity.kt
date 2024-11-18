package com.example.testfirebase

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var listView: ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val stockList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance("https://testfirebase-f4e5c-default-rtdb.firebaseio.com/").reference

        // Initialize ListView and ArrayAdapter
        listView = findViewById(R.id.listView)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stockList)
        listView.adapter = arrayAdapter

        // Set up buttons to load different stocks
        findViewById<Button>(R.id.button).setOnClickListener {
            loadStockData("Stock1")
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            loadStockData("Stock2")
        }
    }

    private fun loadStockData(stockId: String) {
        database.child(stockId).get().addOnSuccessListener { dataSnapshot ->
            stockList.clear() // Clear the current list
            for (itemSnapshot in dataSnapshot.children) {
                val itemName = itemSnapshot.key
                val itemDetails = itemSnapshot.value.toString()
                stockList.add("$itemName: $itemDetails")
            }
            arrayAdapter.notifyDataSetChanged() // Update ListView
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Failed to load data: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }
}