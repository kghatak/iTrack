package com.dol.itrack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dol.itrack.databinding.FragmentProgressWithCardsBinding

class ProgressWithCardsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProgressWithCardsBinding.inflate(inflater, container, false)
        // Initialize progress bars and set progress values
        binding.outerProgressBar.progress = 60f
        binding.innerProgressBar.progress = 30f
        // Set center text
        binding.centerText.text = "50"
        // Setup RecyclerView for cards
        // ...
        return binding.root
    }
}