package com.example.eventmanagementapp.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(eventEntity: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(eventsList: MutableList<EventEntity>)

    @Delete
    suspend fun deleteEvent(eventEntity: EventEntity)

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Int): EventEntity

    @Query("SELECT * FROM events ORDER BY date DESC")
    fun getAll(): LiveData<MutableList<EventEntity>>

    @Query("DELETE FROM events")
    suspend fun deleteAll()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(event: EventEntity)
}