package com.fhanafi.mybottomnavigation.ui.home

import EventAdapter
import SmallEventAdapter
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
    private lateinit var smallEventAdapter: SmallEventAdapter
    private lateinit var viewModel: FinishedViewModel
    private lateinit var viewModelSmall: UpcomingViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(FinishedViewModel::class.java)
        viewModelSmall = ViewModelProvider(this).get(UpcomingViewModel::class.java)

        eventAdapter = EventAdapter { eventId -> onEventClick(eventId) } // Ensure the adapter is initialized
        binding.recyclerViewVertical.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewVertical.adapter = eventAdapter // Set the adapter immediately

        smallEventAdapter = SmallEventAdapter { eventId -> onEventClick(eventId) }
        binding.recyclerViewHorizontal.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) // Set to horizontal
        binding.recyclerViewHorizontal.adapter = smallEventAdapter


        showLoading(true)
        viewModel.fetchEventsFinishFromApi()
        viewModelSmall.fetchEventsUpcomingFromApi()

        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.listEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
            showLoading(false)
        }

        viewModelSmall.listEvents.observe(viewLifecycleOwner) { finishedEvents ->
            smallEventAdapter.submitList(finishedEvents)
            showLoading(false)
        }

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
        val intent = Intent(context, DetailEventActivity::class.java)
        intent.putExtra("EVENT_ID", eventId)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
