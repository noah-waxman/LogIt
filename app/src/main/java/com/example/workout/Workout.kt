package com.example.workout

import java.io.Serializable


class Workout(
    val name: String,
    val reps: Int,
    val weight: Double
    ) : Serializable {
}