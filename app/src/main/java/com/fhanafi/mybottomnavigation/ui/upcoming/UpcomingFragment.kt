package com.fhanafi.mybottomnavigation.ui.upcoming

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

class UpcomingFragment : Fragment() {

    private var _binding: RecyclerViewWithProgressBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UpcomingViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = RecyclerViewWithProgressBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]

        setupRecyclerView()

        observeViewModel()

        viewModel.fetchEventsUpcomingFromApi()

        return binding.root
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter{ eventId ->
            val intent = Intent(context, DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", eventId)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // menambahkan pemisah antara item
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(itemDecoration)

        binding.recyclerView.adapter = eventAdapter

    }

    private fun observeViewModel() {
        viewModel.listEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
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
