package com.example.workout

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class Add_Workout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Set status bar color to background color
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        super.onCreate(savedInstanceState)

        // Set theme
        setTheme(R.style.SplashTheme)
        setContentView(R.layout.activity_add_workout)

        // Change color of button
        val button = findViewById<Button>(R.id.add_workout_btn)
        button.setBackgroundColor(Color.parseColor("#ffdb1818"))

        // Set listener for add_workout button
        button.setOnClickListener {
            buttonClicked()
        }

    }

    fun buttonClicked() {
        // Get fields
        val nameField = findViewById<TextInputEditText>(R.id.name_text)
        val repsField = findViewById<TextInputEditText>(R.id.rep_text)
        val weightField = findViewById<TextInputEditText>(R.id.weight_text)

        // Validate input
        var inputError = false
        if (nameField.text.toString().isEmpty()) {
            nameField.error = "Exercise name cannot be empty"
            inputError = true
        }
        if (repsField.text.toString().isEmpty()) {
            repsField.error = "Reps cannot be empty"
            inputError = true
        }
        if (weightField.text.toString().isEmpty()) {
            weightField.error = "Weight cannot be empty"
            inputError = true
        }
        if (repsField.text.toString().toIntOrNull() == null) {
            repsField.error = "Must be a number"
            inputError = true
        }
        if (weightField.text.toString().toDoubleOrNull() == null) {
            weightField.error = "Must be a number"
            inputError = true
        }

        if (!inputError) {
            val nameField = findViewById<TextInputEditText>(R.id.name_text)
            val repsField = findViewById<TextInputEditText>(R.id.rep_text)
            val weightField = findViewById<TextInputEditText>(R.id.weight_text)

            val name = nameField.text.toString()
            val reps = repsField.text.toString().toInt()
            val weight = weightField.text.toString().toDouble()
            val workout = Workout(name, reps, weight)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("KEY_WORKOUT", workout)
            startActivity(intent)
            overridePendingTransition(R.anim.sl, R.anim.sr)

            finish()
        }
    }

    // If the user presses the back button then an animation will play
    override fun onBackPressed() {
        // Call the super method to allow the system to handle the back button press as well
        super.onBackPressed()

        // Use the overridePendingTransition method to play the animation when the user transitions to the previous activity
        overridePendingTransition(R.anim.sl, R.anim.sr)
    }

}