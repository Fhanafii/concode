package com.fhanafi.mybottomnavigation.ui.finished

import EventAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.mybottomnavigation.R
import com.fhanafi.mybottomnavigation.data.response.ListEventsItem
import com.fhanafi.mybottomnavigation.data.retrofit.ApiConfig
import com.fhanafi.mybottomnavigation.databinding.RecyclerViewWithProgressBinding
import com.fhanafi.mybottomnavigation.ui.detail.DetailEventActivity
import kotlinx.coroutines.launch

class FinishedFragment : Fragment() {

    private var _binding: RecyclerViewWithProgressBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FinishedViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = RecyclerViewWithProgressBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[FinishedViewModel::class.java]

        setupRecyclerView()

        observeViewModel()

        viewModel.fetchEventsFinishFromApi()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchEventByQuery(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isEmpty()) {
                        viewModel.fetchEventsFinishFromApi()
                    } else {
                        searchEventByQuery(it)
                    }
                }
                return true
            }
        })

        searchView?.setOnCloseListener {
            viewModel.fetchEventsFinishFromApi()
            false
        }
    }


    private fun searchEventByQuery(query: String) {
        lifecycleScope.launch {
            try {
                Log.d("Search", "Making API request for query: $query")

                val response = ApiConfig.getApiService().searchEvents(query = query)

                if (response.isSuccessful) {
                    response.body()?.let {
                        updateEventList(it.listEvents)  // Update RecyclerView with search results
                        Log.d("Search", "Received ${it.listEvents.size} results")
                    }
                } else {
                    Log.e("Search", "API response error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FinishedFragment", "Error fetching search results: ${e.message}")
            }
        }
    }

    private fun updateEventList(events: List<ListEventsItem>) {
        eventAdapter.submitList(events)
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { eventId ->
            val intent = Intent(context, DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", eventId) // Ensure eventId is of type String
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

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
