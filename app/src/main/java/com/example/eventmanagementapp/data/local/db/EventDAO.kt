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

    /**
     * Inserts a single event into the database. If an event with the same ID already exists,
     * it replaces the old entry.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(eventEntity: EventEntity)

    /**
     * Inserts a list of events into the database. If events with the same ID already exist,
     * they will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(eventsList: MutableList<EventEntity>)

    /**
     * Deletes a single event from the database.
     */
    @Delete
    suspend fun deleteEvent(eventEntity: EventEntity)

    /**
     * Retrieves an event from the database based on its ID.
     */
    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Int): EventEntity

    /**
     * Retrieves all events from the database, ordered by date in descending order. The results
     * are observed using LiveData.
     */
    @Query("SELECT * FROM events ORDER BY date DESC")
    fun getAll(): LiveData<MutableList<EventEntity>>

    /**
     * Deletes all events from the database.
     */
    @Query("DELETE FROM events")
    suspend fun deleteAll()

    /**
     * Updates an existing event in the database. If the event does not exist, it will be inserted.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(event: EventEntity)
}
