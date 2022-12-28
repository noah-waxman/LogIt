package com.example.workout

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.workout.R.anim
import android.app.Activity
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecyclerAdapter(context: Context, private val dbHelper: SQLiteDatabaseHelper) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var workouts: List<Workout> = emptyList()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val workouts = dbHelper.getWorkouts()
            withContext(Dispatchers.Main) {
                this@RecyclerAdapter.workouts = workouts
                notifyDataSetChanged()
            }
        }
    }


    override fun getItemCount(): Int {
        return workouts.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_cell, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val workout = workouts[position]
        holder.itemName.text = workout.name
        holder.itemRep.text = "${workout.reps} reps"  // add " reps" to reps value
        holder.itemWeight.text = "${workout.weight} lbs"  // add " lbs" to weight value

        val cellCardView = holder.itemView.findViewById<CardView>(R.id.card_cell_view)

        cellCardView.setOnClickListener {
            // Start a new activity and pass the workout object as an extra
            val intent = Intent(holder.itemView.context, Edit_Workout::class.java).apply {
                putExtra("KEY_WORKOUT", workout)
            }
            holder.itemView.context.startActivity(intent)
            (holder.itemView.context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemName: TextView
        var itemRep: TextView
        var itemWeight: TextView

        init {
            itemName = itemView.findViewById(R.id.workout_name)
            itemRep = itemView.findViewById(R.id.rep_amt)
            itemWeight = itemView.findViewById(R.id.weight_amt)
        }
    }

    fun addWorkout(workout: Workout) {
        CoroutineScope(Dispatchers.IO).launch {
            dbHelper.addWorkout(workout)
            withContext(Dispatchers.Main) {
                workouts += workout
                notifyDataSetChanged()
            }
        }
    }

    fun refreshDataset() {
        workouts = dbHelper.getWorkouts()
        notifyDataSetChanged()
    }


    fun editWorkout(originalWorkout: Workout, workout: Workout) {
        CoroutineScope(Dispatchers.IO).launch {
            dbHelper.editWorkout(workout, originalWorkout)
            withContext(Dispatchers.Main) {
                refreshDataset()
            }
        }
    }
}


