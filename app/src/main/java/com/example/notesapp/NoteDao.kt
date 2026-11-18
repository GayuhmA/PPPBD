package com.example.notesapp
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao

interface NoteDao {
    // READ: diamati terus-menerus menggunakan Flow
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAll(): Flow<List<Note>>
    // CREATE
    @Insert
    suspend fun insert(note: Note)
    // UPDATE
    @Update
    suspend fun update(note: Note)
    // DELETE
    @Delete
    suspend fun delete(note: Note)
}