package com.fhanafi.mybottomnavigation.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fhanafi.mybottomnavigation.data.EventRepository
import com.fhanafi.mybottomnavigation.data.local.entity.EventEntity

class FavoriteViewModel(private val repository: EventRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _favoriteEvents = MutableLiveData<List<EventEntity>>()
    val favoriteEvents: LiveData<List<EventEntity>> get() = _favoriteEvents

    fun fetchFavoriteEvents() {
        _isLoading.value = true

        repository.getFavoriteEvents().observeForever { events ->
            _favoriteEvents.value = events
            _isLoading.value = false
        }
    }
}