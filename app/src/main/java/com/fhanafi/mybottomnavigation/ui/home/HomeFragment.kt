package com.fhanafi.mybottomnavigation.ui.home

import EventAdapter
import SmallEventAdapter // Make sure to import your new adapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.mybottomnavigation.databinding.FragmentHomeBinding
import com.fhanafi.mybottomnavigation.ui.detail.DetailEventActivity
import com.fhanafi.mybottomnavigation.ui.finished.FinishedViewModel
import com.fhanafi.mybottomnavigation.ui.upcoming.UpcomingViewModel

class HomeFragment : Fragment() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var smallEventAdapter: SmallEventAdapter // Declare the new adapter
    private lateinit var viewModel: FinishedViewModel
    private lateinit var viewModelSmall: UpcomingViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize the ViewModel for events
        viewModel = ViewModelProvider(this).get(FinishedViewModel::class.java)
        // Initialize the ViewModel for small events
        viewModelSmall = ViewModelProvider(this).get(UpcomingViewModel::class.java)

        // Initialize the adapter and set it to the RecyclerView
        eventAdapter = EventAdapter { eventId -> onEventClick(eventId) } // Ensure the adapter is initialized
        binding.recyclerViewHorizontal.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHorizontal.adapter = eventAdapter // Set the adapter immediately

        // Initialize the new adapter for the vertical RecyclerView
        smallEventAdapter = SmallEventAdapter { eventId -> onEventClick(eventId) }
        binding.recyclerViewVertical.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) // Set to horizontal
        binding.recyclerViewVertical.adapter = smallEventAdapter

        // Start loading when fetching events
        showLoading(true)
        // Fetch events from the API
        viewModel.fetchEventsFinishFromApi()
        viewModelSmall.fetchEventsUpcomingFromApi()

        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        // Observe events for the horizontal RecyclerView
        viewModel.listEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
            showLoading(false) // Stop loading after events are loaded
        }

        // Observe finished events for the vertical RecyclerView
        viewModelSmall.listEvents.observe(viewLifecycleOwner) { finishedEvents ->
            smallEventAdapter.submitList(finishedEvents)
            showLoading(false) // Stop loading after finished events are loaded
        }

        // Observe the loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        viewModelSmall.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar1.visibility = View.VISIBLE
        } else {
            binding.progressBar1.visibility = View.GONE
        }
    }

    private fun onEventClick(eventId: String) {
        // Handle click event
        val intent = Intent(context, DetailEventActivity::class.java)
        intent.putExtra("EVENT_ID", eventId)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
