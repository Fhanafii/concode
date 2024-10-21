package com.fhanafi.mybottomnavigation.ui.favorite

import EventAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.mybottomnavigation.data.local.room.EventDatabase
import com.fhanafi.mybottomnavigation.data.remote.response.ListEventsItem
import com.fhanafi.mybottomnavigation.databinding.FragmentFavoriteBinding

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
        val viewModelFactory = FavoriteViewModelFactory(database)
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoriteViewModel::class.java]

        adapter = EventAdapter { event ->
            Toast.makeText(requireContext(), "Clicked on", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorite.adapter = adapter

        // Show ProgressBar initially
        binding.progressBarFav.visibility = View.VISIBLE

        viewModel.getFavoriteEvents().observe(viewLifecycleOwner) { users ->
            val items = arrayListOf<ListEventsItem>()
            users.map {
                val item = ListEventsItem(
                    id = it.id.toIntOrNull() ?: 0,
                    name = it.name ?: "",
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
                items.add(item)
            }

            adapter.submitList(items)

            // Show/Hide progress bar and handle empty state
            binding.progressBarFav.visibility = View.GONE
            if (items.isEmpty()) {
                binding.recyclerViewFavorite.visibility = View.GONE
                binding.textFavoriteSubtitle.text = "No favorites found"
            } else {
                binding.recyclerViewFavorite.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
