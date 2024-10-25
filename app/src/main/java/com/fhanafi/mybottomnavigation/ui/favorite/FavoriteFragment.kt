package com.fhanafi.mybottomnavigation.ui.favorite

import EventAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.mybottomnavigation.data.EventRepository
import com.fhanafi.mybottomnavigation.data.local.room.EventDatabase
import com.fhanafi.mybottomnavigation.data.remote.response.ListEventsItem
import com.fhanafi.mybottomnavigation.databinding.FragmentFavoriteBinding
import com.fhanafi.mybottomnavigation.ui.detail.DetailEventActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = EventDatabase.getDatabase(requireContext())
        val repository = EventRepository(database.eventDao())

        val viewModelFactory = FavoriteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoriteViewModel::class.java]

        setupRecyclerView()

        observeViewModel()

        viewModel.fetchFavoriteEvents()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter { eventId ->
            if (eventId.isNotEmpty() && eventId != "0") {
                val intent = Intent(requireContext(), DetailEventActivity::class.java).apply {
                    putExtra("EVENT_ID", eventId)
                }
                startActivity(intent)
            }
        }

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorite.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            println("Loading state changed: $isLoading")
            showLoading(isLoading)
        }
        viewModel.favoriteEvents.observe(viewLifecycleOwner) { events ->
            val items = events.map {
                ListEventsItem(
                    id = it.id.toIntOrNull() ?: 0,
                    name = it.name,
                    imageLogo = it.mediaCover ?: "",
                    summary = "",
                    mediaCover = it.mediaCover ?: "",
                    registrants = 0,
                    link = "",
                    description = "",
                    ownerName = "",
                    cityName = "",
                    quota = 0,
                    beginTime = "",
                    endTime = "",
                    category = ""
                )
            }
            adapter.submitList(items)
            handleEmptyState(items.isEmpty())
        }

    }

    private fun handleEmptyState(isEmpty: Boolean) {
        // Handle empty state UI
        binding.progressBarFav.visibility = View.GONE
        if (isEmpty) {
            binding.recyclerViewFavorite.visibility = View.GONE
            binding.textFavoriteSubtitle.visibility = View.VISIBLE
            binding.textFavoriteSubtitle.text = "No favorites found"
        } else {
            binding.recyclerViewFavorite.visibility = View.VISIBLE
            binding.textFavoriteSubtitle.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarFav.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
