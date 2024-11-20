package com.dol.itrack.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dol.itrack.databinding.DailyActivityCardBinding
import com.dol.itrack.databinding.HeartRateCardBinding

data class DailyActivityData(val steps: Int, val caloriesBurned: Int)
data class HeartRateData(val heartRate: Int)


class HomeCardAdapter(private val cardDataList: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_DAILY_ACTIVITY = 0
        private const val VIEW_TYPE_HEART_RATE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DAILY_ACTIVITY -> {
                val binding = DailyActivityCardBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                DailyActivityViewHolder(binding)
            }
            VIEW_TYPE_HEART_RATE -> {
                val binding = HeartRateCardBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HeartRateViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is DailyActivityViewHolder -> {
                val data = cardDataList[position] as DailyActivityData
                holder.bind(data)
            }
            is HeartRateViewHolder -> {
                val data = cardDataList[position] as HeartRateData
                holder.bind(data)
            }
        }
    }

    override fun getItemCount(): Int = cardDataList.size

    override fun getItemViewType(position: Int): Int {
        return when (cardDataList[position]) {
            is DailyActivityData -> VIEW_TYPE_DAILY_ACTIVITY
            is HeartRateData -> VIEW_TYPE_HEART_RATE
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    inner class DailyActivityViewHolder(private val binding: DailyActivityCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DailyActivityData) {
            binding.stepsCountTextView.text = "Steps: ${data.steps}"
            binding.caloriesBurnedTextView.text = "Calories Burned: ${data.caloriesBurned}"
        }
    }

    inner class HeartRateViewHolder(private val binding: HeartRateCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HeartRateData) {
            binding.heartRateTextView.text = "${data.heartRate} bpm"
        }
    }
}

