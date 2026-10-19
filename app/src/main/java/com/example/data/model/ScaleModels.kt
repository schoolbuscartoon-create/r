package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Entity(tableName = "family_characters")
data class FamilyCharacter(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val role: String, // "Ayah", "Ibu", "Anak", "Kakek", "Nenek", "Saudara", "Pribadi"
    val gender: String, // "Laki-laki" or "Perempuan"
    val dob: String, // "HH-BBB-TTTT", e.g. "15-Jan-1985"
    val age: Int,
    val heightCm: Double, // e.g. 170.0
    val defaultWeightKg: Double, // e.g. 65.50
    val defaultBmi: Double, // e.g. 22.66
    val avatarEmoji: String = "👤",
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "scale_records")
data class ScaleRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val characterId: Long,
    val characterName: String,
    val characterRole: String,
    val gender: String,
    val age: Int,
    val dob: String,
    val weightKg: Double,
    val heightCm: Double,
    val bmi: Double,
    val bmiCategory: String, // "Kurus", "Normal / Ideal", "Kelebihan Berat", "Obesitas"
    val dateStr: String, // "19-Okt-2026"
    val timeStr: String, // "09:54 WIB"
    val photoUri: String? = null,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

object ScaleUtils {
    private val MONTH_NAMES_ID = listOf(
        "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
        "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
    )

    fun getIndonesianMonths(): List<String> = MONTH_NAMES_ID

    fun formatWeightDigital(weightKg: Double): String {
        // Formats as 000000.00 standard requested by user: "Berat 000000.00"
        val total = (weightKg * 100).toLong().coerceAtLeast(0)
        val integerPart = total / 100
        val decimalPart = total % 100
        return String.format(Locale.US, "%06d.%02d", integerPart, decimalPart)
    }

    fun formatWeightShort(weightKg: Double): String {
        return String.format(Locale.US, "%.2f", weightKg)
    }

    fun formatBmiDigital(bmi: Double): String {
        // Formats as 000000.00 standard requested by user: "BMI 000000.00"
        val total = (bmi * 100).toLong().coerceAtLeast(0)
        val integerPart = total / 100
        val decimalPart = total % 100
        return String.format(Locale.US, "%06d.%02d", integerPart, decimalPart)
    }

    fun formatBmiShort(bmi: Double): String {
        return String.format(Locale.US, "%.2f", bmi)
    }

    fun formatHeight(heightCm: Double): String {
        // Formats with .0 cm requested by user: "Tinggi .0 cm"
        return String.format(Locale.US, "%.1f cm", heightCm)
    }

    fun calculateBmi(weightKg: Double, heightCm: Double): Double {
        if (heightCm <= 0.0) return 0.0
        val heightM = heightCm / 100.0
        return weightKg / (heightM * heightM)
    }

    fun getBmiCategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Kurus"
            bmi < 25.0 -> "Normal / Ideal"
            bmi < 30.0 -> "Kelebihan Berat"
            else -> "Obesitas"
        }
    }

    fun getIdealWeightRange(heightCm: Double): Pair<Double, Double> {
        val heightM = heightCm / 100.0
        val minIdeal = 18.5 * (heightM * heightM)
        val maxIdeal = 24.9 * (heightM * heightM)
        return Pair(minIdeal, maxIdeal)
    }

    fun currentDateIndonesian(): String {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val monthIdx = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        val monthStr = MONTH_NAMES_ID.getOrElse(monthIdx) { "Bln" }
        return String.format(Locale.US, "%02d-%s-%04d", day, monthStr, year)
    }

    fun currentTimeIndonesian(): String {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE)
        return String.format(Locale.US, "%02d:%02d WIB", hour, min)
    }

    fun formatDob(day: Int, monthIdx: Int, year: Int): String {
        val monthStr = MONTH_NAMES_ID.getOrElse(monthIdx) { "Jan" }
        return String.format(Locale.US, "%02d-%s-%04d", day, monthStr, year)
    }

    fun calculateAgeFromDob(dob: String): Int {
        // DOB format: "HH-BBB-TTTT" e.g. "15-Jan-1985"
        val parts = dob.split("-")
        if (parts.size != 3) return 25
        val year = parts[2].toIntOrNull() ?: return 25
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return (currentYear - year).coerceIn(0, 120)
    }
}
