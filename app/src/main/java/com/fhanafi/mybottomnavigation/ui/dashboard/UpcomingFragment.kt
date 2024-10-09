package com.fhanafi.mybottomnavigation.ui.dashboard

import EventAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.mybottomnavigation.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UpcomingViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        // Setup ViewModel
        viewModel = ViewModelProvider(this).get(UpcomingViewModel::class.java)

        // Setup RecyclerView and Adapter
        setupRecyclerView()

        // Observe LiveData
        observeViewModel()

        // Trigger data fetch
        viewModel.fetchEventsFromApi()

        return binding.root
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = eventAdapter
    }

    private fun observeViewModel() {
        viewModel.listEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
