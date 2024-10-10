package com.fhanafi.mybottomnavigation.ui.notifications

import EventAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.mybottomnavigation.databinding.RecyclerViewWithProgressBinding
import com.fhanafi.mybottomnavigation.ui.detail.DetailEventActivity

class NotificationsFragment : Fragment() {

    private var _binding: RecyclerViewWithProgressBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotificationsViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = RecyclerViewWithProgressBinding.inflate(inflater, container, false)

        // Setup ViewModel
        viewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]

        // Setup RecyclerView and Adapter
        setupRecyclerView()

        // Observe LiveData
        observeViewModel()

        // Trigger data fetch
        viewModel.fetchEventsFinishFromApi()

        return binding.root
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter{ eventId ->
            val intent = Intent(context, DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", eventId) // Ensure eventId is of type String
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Adding divider between items
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)

        binding.recyclerView.adapter = eventAdapter
    }

    private fun observeViewModel() {
        viewModel.listEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
            // Hide the ProgressBar when data is received
//            showLoading(false)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}