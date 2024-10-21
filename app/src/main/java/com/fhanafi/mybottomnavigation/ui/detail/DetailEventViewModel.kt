package com.fhanafi.mybottomnavigation.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.mybottomnavigation.data.local.entity.EventEntity
import com.fhanafi.mybottomnavigation.data.local.room.EventDatabase
import com.fhanafi.mybottomnavigation.data.remote.response.DetailEventResponse
import com.fhanafi.mybottomnavigation.data.remote.response.ListEventsItem
import com.fhanafi.mybottomnavigation.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {
    private val _detailEvent = MutableLiveData<ListEventsItem>()
    val detailEvent: LiveData<ListEventsItem> get() = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    companion object {
        const val TAG = "DetailEventViewModel"
    }

    private val apiService = ApiConfig.getApiService()

    fun getDetailEvent(eventId: String) {
        _isLoading.value = true
        apiService.getDetailEvent(eventId).enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(call: Call<DetailEventResponse>, response: Response<DetailEventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.event?.let { event ->
                        _detailEvent.postValue(event)
                    } ?: run {
                        Log.e(TAG, "No event found for ID: $eventId")
                    }
                } else {
                    Log.e(TAG, "Failed to fetch event details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    // Check if an event is already marked as favorite in the local database
    fun checkIfFavorite(eventId: String, database: EventDatabase) {
        viewModelScope.launch {
            val favoriteEvent = database.eventDao().getFavoriteEventById(eventId)
            _isFavorite.postValue(favoriteEvent != null)
        }
    }

    // Toggle favorite status for an event
    fun toggleFavoriteStatus(event: ListEventsItem, database: EventDatabase) {
        viewModelScope.launch {
            val favoriteEvent = EventEntity(
                id = event.id.toString(),
                name = event.name,
                mediaCover = event.mediaCover
            )

            val existingEvent = database.eventDao().getFavoriteEventById(event.id.toString())
            if (existingEvent == null) {
                // Add to favorites
                database.eventDao().insertFavoriteEvent(favoriteEvent)
                _isFavorite.postValue(true)
            } else {
                // Remove from favorites
                database.eventDao().deleteFavoriteEventById(event.id.toString())
                _isFavorite.postValue(false)
            }
        }
    }
}
