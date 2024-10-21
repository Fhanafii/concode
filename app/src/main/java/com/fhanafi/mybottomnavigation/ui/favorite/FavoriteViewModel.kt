package com.fhanafi.mybottomnavigation.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fhanafi.mybottomnavigation.data.EventRepository
import com.fhanafi.mybottomnavigation.data.local.entity.EventEntity

class FavoriteViewModel(private val repository: EventRepository): ViewModel() {

    // Get all favorite events from the repository
    fun getFavoriteEvents(): LiveData<List<EventEntity>> {
        return repository.getFavoriteEvents()
    }
}