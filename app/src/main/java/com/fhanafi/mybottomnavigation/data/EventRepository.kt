package com.fhanafi.mybottomnavigation.data

import androidx.lifecycle.LiveData
import com.fhanafi.mybottomnavigation.data.local.entity.EventEntity
import com.fhanafi.mybottomnavigation.data.local.room.EventDao

class EventRepository(private val eventDao: EventDao) {

    fun getFavoriteEvents(): LiveData<List<EventEntity>> {
        return eventDao.getAllFavoriteEvents()
    }
}