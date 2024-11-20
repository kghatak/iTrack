package com.dol.itrack.ui.home

import android.animation.TimeInterpolator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dol.itrack.databinding.FragmentHomeBinding
import com.dol.itrack.databinding.FragmentProgressWithCardsBinding

class HomeFragment : Fragment() {

    private lateinit var homeCardAdapter: HomeCardAdapter
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProgressWithCardsBinding.inflate(inflater, container, false)
        // Initialize progress bars and set progress values
        // Animate progress bars with duration and interpolator
        binding.outerProgressBar.setProgressWithAnimation(60f, 1000,
            AccelerateDecelerateInterpolator() as TimeInterpolator
        )
        binding.innerProgressBar.setProgressWithAnimation(30f, 1000,
            AccelerateDecelerateInterpolator() as TimeInterpolator)
        // Set center text
        binding.centerText.text = "50"
        // Setup RecyclerView for cards
        // ...

        albumAdapter = AlbumAdapter()
        binding.albumRecyclerView.adapter = albumAdapter
        binding.albumRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val cardDataList = listOf(
            DailyActivityData(8500, 350),
            HeartRateData(72),
            DailyActivityData(8500, 350),
            HeartRateData(72),
            DailyActivityData(8500, 350),
            HeartRateData(72),
            DailyActivityData(8500, 350),
            HeartRateData(72),
            // ... other card data
        )

        // Initialize and set up the adapter
        homeCardAdapter = HomeCardAdapter(cardDataList)
        binding.cardRecyclerView.adapter = homeCardAdapter
        binding.cardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        //binding.cardRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)


        return binding.root
    }

//    private var _binding: FragmentHomeBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
//
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}