package com.example.foodfinder

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    lateinit var name: EditText
    lateinit var location: EditText
    lateinit var rating: EditText
    lateinit var addBtn: Button
    lateinit var showBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().getReference("Restaurants")

        name = findViewById(R.id.etName)
        location = findViewById(R.id.etLocation)
        rating = findViewById(R.id.etRating)

        addBtn = findViewById(R.id.btnAdd)
        showBtn = findViewById(R.id.btnShow)

        addBtn.setOnClickListener {
            addRestaurant()
        }

        showBtn.setOnClickListener {
            readRestaurants()
        }
    }

    private fun addRestaurant() {

        val restaurantId = database.push().key!!

        val restaurant = Restaurant(
            name.text.toString(),
            location.text.toString(),
            rating.text.toString().toDouble()
        )

        database.child(restaurantId).setValue(restaurant)
    }

    private fun readRestaurants() {

        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (restaurantSnapshot in snapshot.children) {

                    val restaurant = restaurantSnapshot.getValue(Restaurant::class.java)

                    Log.d("FirebaseData", "Name: ${restaurant?.name}")
                    Log.d("FirebaseData", "Location: ${restaurant?.location}")
                    Log.d("FirebaseData", "Rating: ${restaurant?.rating}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", error.message)
            }
        })
    }
}