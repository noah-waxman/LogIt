package com.example.workout

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set status bar color to match background color
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        // Set theme and content view
        setTheme(R.style.SplashTheme)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView and adapter
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val dbHelper = SQLiteDatabaseHelper(this)
        val adapter = RecyclerAdapter(this, dbHelper)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Find the FAB and set an OnClickListener for it
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        // Change color of FAB
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdb1818")))

        // Start FAB listener
        fab.setOnClickListener {
            startNewActivity()
        }

        // Retrieve workout passed in intent, if any
        var thisWorkout: Workout? = intent.getSerializableExtra("KEY_WORKOUT") as? Workout

        // Add workout to adapter, if it exists
        if (thisWorkout != null) {
            adapter.addWorkout(thisWorkout)
        }

        var newWorkout: Workout? = intent.getSerializableExtra("KEY_EDIT_WORKOUT") as? Workout
        var originalWorkout: Workout? = intent.getSerializableExtra("KEY_ORIGINAL_WORKOUT") as? Workout
        if (newWorkout != null) {
            if (originalWorkout != null) {
                adapter.editWorkout(originalWorkout, newWorkout)
            }
        }
    }

    // Add workout activity
    fun startNewActivity() {
        // Create an Intent to start the new activity
        val intent = Intent(this, Add_Workout::class.java)
        // Start the new activity
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
    }
}