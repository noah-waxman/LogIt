package com.example.workout

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        // Database Version
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "WorkoutDB"

        // Table Name
        private const val TABLE_WORKOUTS = "workouts"

        // Table Columns names
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_REPS = "reps"
        private const val KEY_WEIGHT = "weight"
    }

    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_WORKOUTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_REPS + " INTEGER,"
                + KEY_WEIGHT + " REAL" + ")")
        db.execSQL(createTable)
    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKOUTS")
        // Create tables again
        onCreate(db)
    }

    // Adding new workout
    fun addWorkout(workout: Workout): Long {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_NAME, workout.name)
        values.put(KEY_REPS, workout.reps)
        values.put(KEY_WEIGHT, workout.weight)

        // Inserting Row
        val success = db.insert(TABLE_WORKOUTS, null, values)
        return success
    }

    fun editWorkout(workout: Workout, originalWorkout: Workout) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_NAME, workout.name)
        values.put(KEY_REPS, workout.reps)
        values.put(KEY_WEIGHT, workout.weight)

        // Update the row with the matching name
        db.update(TABLE_WORKOUTS, values, "$KEY_NAME = ?", arrayOf(originalWorkout.name))
    }

    fun getWorkouts(): List<Workout> {
        val workouts = mutableListOf<Workout>()
        val db = this.readableDatabase
        val cursor = db.query(
            "workouts",  // Table name
            arrayOf("name", "reps", "weight"),  // Columns to return
            null,  // WHERE clause
            null,  // WHERE arguments
            null,  // GROUP BY clause
            null,  // HAVING clause
            null  // ORDER BY clause
        )
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val nameColumnIndex = cursor.getColumnIndex("name")
            val repsColumnIndex = cursor.getColumnIndex("reps")
            val weightColumnIndex = cursor.getColumnIndex("weight")
            if (nameColumnIndex >= 0 && repsColumnIndex >= 0 && weightColumnIndex >= 0) {
                val workout = Workout(
                    cursor.getString(nameColumnIndex),
                    cursor.getInt(repsColumnIndex),
                    cursor.getDouble(weightColumnIndex)
                )
                workouts.add(workout)
            }
            cursor.moveToNext()
        }
        cursor.close()
        return workouts
    }

    fun removeWorkout(workout: Workout) {
        val db = this.writableDatabase
        db.delete(TABLE_WORKOUTS, "$KEY_NAME = ?", arrayOf(workout.name))
    }
}
