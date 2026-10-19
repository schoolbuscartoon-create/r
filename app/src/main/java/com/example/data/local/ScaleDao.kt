package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FamilyCharacter
import com.example.data.model.ScaleRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ScaleDao {

    // Characters
    @Query("SELECT * FROM family_characters ORDER BY id ASC")
    fun getAllCharacters(): Flow<List<FamilyCharacter>>

    @Query("SELECT * FROM family_characters WHERE id = :id LIMIT 1")
    suspend fun getCharacterById(id: Long): FamilyCharacter?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: FamilyCharacter): Long

    @Update
    suspend fun updateCharacter(character: FamilyCharacter)

    @Delete
    suspend fun deleteCharacter(character: FamilyCharacter)

    @Query("SELECT COUNT(*) FROM family_characters")
    suspend fun getCharacterCount(): Int

    // Scale Records
    @Query("SELECT * FROM scale_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<ScaleRecord>>

    @Query("SELECT * FROM scale_records WHERE characterId = :characterId ORDER BY timestamp DESC")
    fun getRecordsForCharacter(characterId: Long): Flow<List<ScaleRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: ScaleRecord): Long

    @Delete
    suspend fun deleteRecord(record: ScaleRecord)

    @Query("DELETE FROM scale_records WHERE characterId = :characterId")
    suspend fun deleteRecordsForCharacter(characterId: Long)
}
