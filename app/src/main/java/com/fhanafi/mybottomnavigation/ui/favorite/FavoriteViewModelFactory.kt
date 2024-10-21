package com.fhanafi.mybottomnavigation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fhanafi.mybottomnavigation.data.EventRepository
import com.fhanafi.mybottomnavigation.data.local.room.EventDatabase

class FavoriteViewModelFactory(private val database: EventDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            val repository = EventRepository(database)
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
