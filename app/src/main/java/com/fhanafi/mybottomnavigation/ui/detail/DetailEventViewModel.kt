package com.fhanafi.mybottomnavigation.ui.detail

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fhanafi.mybottomnavigation.data.response.DetailEventResponse
import com.fhanafi.mybottomnavigation.data.response.ListEventsItem
import com.fhanafi.mybottomnavigation.data.retrofit.ApiConfig
import com.fhanafi.mybottomnavigation.databinding.ActivityDetailEventBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {
    private val _detailEvent = MutableLiveData<ListEventsItem>()
    val detailEvent: LiveData<ListEventsItem> get() = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    companion object {
        const val TAG = "DetailEventViewModel"
    }

    private val apiService = ApiConfig.getApiService()

    fun getDetailEvent(eventId: String) {
        _isLoading.value = true // Set loading to true when starting the API call
        apiService.getDetailEvent(eventId).enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(call: Call<DetailEventResponse>, response: Response<DetailEventResponse>) {
                _isLoading.value = false // Set loading to false when API call is finished
                if (response.isSuccessful) {
                    // Use the event object directly from DetailEventResponse
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
                _isLoading.value = false // Set loading to false on failure
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
