package com.fhanafi.mybottomnavigation.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fhanafi.mybottomnavigation.data.remote.response.ListEventResponse
import com.fhanafi.mybottomnavigation.data.remote.response.ListEventsItem
import com.fhanafi.mybottomnavigation.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> get() = _listEvents

    companion object {
        private const val TAG = "UpcomingViewModel"
        const val ACTIVE = 1
    }

    fun fetchEventsUpcomingFromApi() {
        _isLoading.value = true

        // Modify the API call to exclude the query parameter
        val client = ApiConfig.getApiService().getEvents(ACTIVE.toString())

        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(call: Call<ListEventResponse>, response: Response<ListEventResponse>) {
                _isLoading.value= false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _listEvents.value = it.listEvents
                        Log.d(TAG, "Events received: ${it.listEvents.size}")
                    } ?: run {
                        Log.e(TAG, "onResponse: No data found")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
