package com.fhanafi.mybottomnavigation.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fhanafi.mybottomnavigation.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteEvent(event: EventEntity)

    @Query("SELECT * FROM FavoriteEvent WHERE id = :id")
    suspend fun getFavoriteEventById(id: String): EventEntity?

    @Query("SELECT * FROM FavoriteEvent")
    fun getAllFavoriteEvents(): LiveData<List<EventEntity>>

    @Query("DELETE FROM FavoriteEvent WHERE id = :id")
    suspend fun deleteFavoriteEventById(id: String)
}
