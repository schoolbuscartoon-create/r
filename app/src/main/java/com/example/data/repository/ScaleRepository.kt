package com.example.data.repository

import com.example.data.local.ScaleDao
import com.example.data.model.FamilyCharacter
import com.example.data.model.ScaleRecord
import com.example.data.model.ScaleUtils
import kotlinx.coroutines.flow.Flow

class ScaleRepository(private val scaleDao: ScaleDao) {

    val allCharacters: Flow<List<FamilyCharacter>> = scaleDao.getAllCharacters()
    val allRecords: Flow<List<ScaleRecord>> = scaleDao.getAllRecords()

    fun getRecordsForCharacter(characterId: Long): Flow<List<ScaleRecord>> {
        return scaleDao.getRecordsForCharacter(characterId)
    }

    suspend fun insertCharacter(character: FamilyCharacter): Long {
        return scaleDao.insertCharacter(character)
    }

    suspend fun updateCharacter(character: FamilyCharacter) {
        scaleDao.updateCharacter(character)
    }

    suspend fun deleteCharacter(character: FamilyCharacter) {
        scaleDao.deleteCharacter(character)
        scaleDao.deleteRecordsForCharacter(character.id)
    }

    suspend fun insertRecord(record: ScaleRecord): Long {
        val recordId = scaleDao.insertRecord(record)
        // Also update character's latest weight & BMI
        val character = scaleDao.getCharacterById(record.characterId)
        if (character != null) {
            val updated = character.copy(
                defaultWeightKg = record.weightKg,
                defaultBmi = record.bmi
            )
            scaleDao.updateCharacter(updated)
        }
        return recordId
    }

    suspend fun deleteRecord(record: ScaleRecord) {
        scaleDao.deleteRecord(record)
    }

    suspend fun prepopulateIfEmpty() {
        if (scaleDao.getCharacterCount() == 0) {
            val ayah = FamilyCharacter(
                name = "Ayah Budi",
                role = "Ayah",
                gender = "Laki-laki",
                dob = "15-Jan-1985",
                age = ScaleUtils.calculateAgeFromDob("15-Jan-1985"),
                heightCm = 172.0,
                defaultWeightKg = 72.80,
                defaultBmi = ScaleUtils.calculateBmi(72.80, 172.0),
                avatarEmoji = "👨"
            )
            val ayahId = scaleDao.insertCharacter(ayah)

            val ibu = FamilyCharacter(
                name = "Ibu Siti",
                role = "Ibu",
                gender = "Perempuan",
                dob = "22-Mei-1988",
                age = ScaleUtils.calculateAgeFromDob("22-Mei-1988"),
                heightCm = 160.0,
                defaultWeightKg = 56.50,
                defaultBmi = ScaleUtils.calculateBmi(56.50, 160.0),
                avatarEmoji = "👩"
            )
            val ibuId = scaleDao.insertCharacter(ibu)

            val anak = FamilyCharacter(
                name = "Kakak Rian",
                role = "Anak",
                gender = "Laki-laki",
                dob = "10-Jul-2015",
                age = ScaleUtils.calculateAgeFromDob("10-Jul-2015"),
                heightCm = 145.0,
                defaultWeightKg = 38.00,
                defaultBmi = ScaleUtils.calculateBmi(38.00, 145.0),
                avatarEmoji = "👦"
            )
            val anakId = scaleDao.insertCharacter(anak)

            // Seed sample records
            val now = System.currentTimeMillis()
            val dayMillis = 86400000L

            scaleDao.insertRecord(
                ScaleRecord(
                    characterId = ayahId,
                    characterName = ayah.name,
                    characterRole = ayah.role,
                    gender = ayah.gender,
                    age = ayah.age,
                    dob = ayah.dob,
                    weightKg = 74.00,
                    heightCm = ayah.heightCm,
                    bmi = ScaleUtils.calculateBmi(74.00, ayah.heightCm),
                    bmiCategory = ScaleUtils.getBmiCategory(ScaleUtils.calculateBmi(74.00, ayah.heightCm)),
                    dateStr = "12-Okt-2026",
                    timeStr = "07:15 WIB",
                    timestamp = now - 7 * dayMillis,
                    notes = "Pagi hari sebelum sarapan"
                )
            )

            scaleDao.insertRecord(
                ScaleRecord(
                    characterId = ayahId,
                    characterName = ayah.name,
                    characterRole = ayah.role,
                    gender = ayah.gender,
                    age = ayah.age,
                    dob = ayah.dob,
                    weightKg = 72.80,
                    heightCm = ayah.heightCm,
                    bmi = ScaleUtils.calculateBmi(72.80, ayah.heightCm),
                    bmiCategory = ScaleUtils.getBmiCategory(ScaleUtils.calculateBmi(72.80, ayah.heightCm)),
                    dateStr = ScaleUtils.currentDateIndonesian(),
                    timeStr = ScaleUtils.currentTimeIndonesian(),
                    timestamp = now,
                    notes = "Setelah sesi jogging sore"
                )
            )

            scaleDao.insertRecord(
                ScaleRecord(
                    characterId = ibuId,
                    characterName = ibu.name,
                    characterRole = ibu.role,
                    gender = ibu.gender,
                    age = ibu.age,
                    dob = ibu.dob,
                    weightKg = 56.50,
                    heightCm = ibu.heightCm,
                    bmi = ScaleUtils.calculateBmi(56.50, ibu.heightCm),
                    bmiCategory = ScaleUtils.getBmiCategory(ScaleUtils.calculateBmi(56.50, ibu.heightCm)),
                    dateStr = ScaleUtils.currentDateIndonesian(),
                    timeStr = "08:30 WIB",
                    timestamp = now - 3600000L,
                    notes = "Timbangan rutin mingguan"
                )
            )
        }
    }
}
