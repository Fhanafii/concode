package com.fhanafi.mybottomnavigation.ui.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fhanafi.mybottomnavigation.data.response.ListEventResponse
import com.fhanafi.mybottomnavigation.data.response.ListEventsItem
import com.fhanafi.mybottomnavigation.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> get() = _listEvents

    companion object {
        private const val TAG = "DashboardViewModel"
        const val ACTIVE_FINISH = 0
        const val QUERY_FINISH = "devcoach"
    }

    // Expose fetch method, don't call it directly in init
    fun fetchEventsFinishFromApi() {
        _isLoading.value = true // Set loading to true
        val client = ApiConfig.getApiService().getEventsWithQuery(ACTIVE_FINISH.toString(), QUERY_FINISH)
        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(
                call: Call<ListEventResponse>,
                response: Response<ListEventResponse>
            ) {
                _isLoading.value = false // Set loading to false
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
                _isLoading.value = false // Set loading to false on failure
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}