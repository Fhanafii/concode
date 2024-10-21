package com.fhanafi.mybottomnavigation.data

import androidx.lifecycle.LiveData
import com.fhanafi.mybottomnavigation.data.local.entity.EventEntity
import com.fhanafi.mybottomnavigation.data.local.room.EventDatabase

class EventRepository(private val database: EventDatabase) {
    fun getFavoriteEvents(): LiveData<List<EventEntity>> {
        return database.eventDao().getAllFavoriteEvents()
    }
}