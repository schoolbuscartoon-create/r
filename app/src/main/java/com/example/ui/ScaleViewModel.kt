package com.example.ui

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ScaleDatabase
import com.example.data.model.FamilyCharacter
import com.example.data.model.ScaleRecord
import com.example.data.model.ScaleUtils
import com.example.data.repository.ScaleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class ScaleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ScaleRepository

    val characters: StateFlow<List<FamilyCharacter>>
    val records: StateFlow<List<ScaleRecord>>

    private val _selectedCharacter = MutableStateFlow<FamilyCharacter?>(null)
    val selectedCharacter: StateFlow<FamilyCharacter?> = _selectedCharacter.asStateFlow()

    // Current scale state
    private val _currentWeight = MutableStateFlow(70.00)
    val currentWeight: StateFlow<Double> = _currentWeight.asStateFlow()

    private val _currentHeight = MutableStateFlow(170.0)
    val currentHeight: StateFlow<Double> = _currentHeight.asStateFlow()

    private val _currentDate = MutableStateFlow(ScaleUtils.currentDateIndonesian())
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()

    private val _currentTime = MutableStateFlow(ScaleUtils.currentTimeIndonesian())
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    private val _uploadedPhotoUri = MutableStateFlow<String?>(null)
    val uploadedPhotoUri: StateFlow<String?> = _uploadedPhotoUri.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    private val _isMeasuring = MutableStateFlow(false)
    val isMeasuring: StateFlow<Boolean> = _isMeasuring.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    // Character Dialog
    private val _showCharacterDialog = MutableStateFlow(false)
    val showCharacterDialog: StateFlow<Boolean> = _showCharacterDialog.asStateFlow()

    private val _editingCharacter = MutableStateFlow<FamilyCharacter?>(null)
    val editingCharacter: StateFlow<FamilyCharacter?> = _editingCharacter.asStateFlow()

    // Full Photo Preview
    private val _previewPhotoUri = MutableStateFlow<String?>(null)
    val previewPhotoUri: StateFlow<String?> = _previewPhotoUri.asStateFlow()

    // History filter by character ID (0 = all)
    private val _historyFilterCharacterId = MutableStateFlow<Long?>(null)
    val historyFilterCharacterId: StateFlow<Long?> = _historyFilterCharacterId.asStateFlow()

    init {
        val db = ScaleDatabase.getDatabase(application)
        repository = ScaleRepository(db.scaleDao())

        characters = repository.allCharacters.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        records = repository.allRecords.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            repository.prepopulateIfEmpty()
        }

        // Auto-select first character once available
        viewModelScope.launch {
            characters.collect { list ->
                if (list.isNotEmpty() && _selectedCharacter.value == null) {
                    selectCharacter(list.first())
                }
            }
        }
    }

    fun selectCharacter(character: FamilyCharacter) {
        _selectedCharacter.value = character
        _currentHeight.value = character.heightCm
        _currentWeight.value = character.defaultWeightKg
        _currentDate.value = ScaleUtils.currentDateIndonesian()
        _currentTime.value = ScaleUtils.currentTimeIndonesian()
    }

    fun setWeight(weight: Double) {
        _currentWeight.value = (weight.coerceIn(5.0, 300.0) * 100).toLong() / 100.0
    }

    fun adjustWeight(delta: Double) {
        val next = (_currentWeight.value + delta).coerceIn(5.0, 300.0)
        _currentWeight.value = (next * 100).toLong() / 100.0
        vibrateLight()
    }

    fun setHeight(height: Double) {
        _currentHeight.value = (height.coerceIn(50.0, 250.0) * 10).toLong() / 10.0
    }

    fun setDate(date: String) {
        _currentDate.value = date
    }

    fun setTime(time: String) {
        _currentTime.value = time
    }

    fun setUploadedPhotoUri(uri: String?) {
        _uploadedPhotoUri.value = uri
    }

    fun setNotes(notes: String) {
        _notes.value = notes
    }

    fun setHistoryFilter(characterId: Long?) {
        _historyFilterCharacterId.value = characterId
    }

    fun openPhotoPreview(uri: String) {
        _previewPhotoUri.value = uri
    }

    fun closePhotoPreview() {
        _previewPhotoUri.value = null
    }

    fun refreshDateTime() {
        _currentDate.value = ScaleUtils.currentDateIndonesian()
        _currentTime.value = ScaleUtils.currentTimeIndonesian()
    }

    fun simulateWeighing() {
        if (_isMeasuring.value) return
        viewModelScope.launch {
            _isMeasuring.value = true
            vibrateLight()
            val targetWeight = _currentWeight.value
            // Fluctuating animation
            val steps = 14
            for (i in 0 until steps) {
                val jitter = (Random.nextDouble(-2.5, 2.5) * (steps - i) / steps)
                _currentWeight.value = ((targetWeight + jitter).coerceAtLeast(10.0) * 100).toLong() / 100.0
                delay(70)
            }
            _currentWeight.value = targetWeight
            _isMeasuring.value = false
            vibrateSuccess()
        }
    }

    fun saveCurrentMeasurement() {
        val character = _selectedCharacter.value ?: return
        val weight = _currentWeight.value
        val height = _currentHeight.value
        val bmi = ScaleUtils.calculateBmi(weight, height)
        val bmiCat = ScaleUtils.getBmiCategory(bmi)

        val record = ScaleRecord(
            characterId = character.id,
            characterName = character.name,
            characterRole = character.role,
            gender = character.gender,
            age = character.age,
            dob = character.dob,
            weightKg = weight,
            heightCm = height,
            bmi = bmi,
            bmiCategory = bmiCat,
            dateStr = _currentDate.value.ifBlank { ScaleUtils.currentDateIndonesian() },
            timeStr = _currentTime.value.ifBlank { ScaleUtils.currentTimeIndonesian() },
            photoUri = _uploadedPhotoUri.value,
            notes = _notes.value
        )

        viewModelScope.launch {
            repository.insertRecord(record)
            _uploadedPhotoUri.value = null
            _notes.value = ""
            _statusMessage.value = "Hasil timbangan berhasil disimpan!"
            vibrateSuccess()
            delay(3000)
            _statusMessage.value = null
        }
    }

    fun deleteRecord(record: ScaleRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
            _statusMessage.value = "Catatan timbangan dihapus"
            delay(2000)
            _statusMessage.value = null
        }
    }

    fun openAddCharacterDialog() {
        _editingCharacter.value = null
        _showCharacterDialog.value = true
    }

    fun openEditCharacterDialog(character: FamilyCharacter) {
        _editingCharacter.value = character
        _showCharacterDialog.value = true
    }

    fun closeCharacterDialog() {
        _showCharacterDialog.value = false
        _editingCharacter.value = null
    }

    fun saveCharacter(
        name: String,
        role: String,
        gender: String,
        dob: String,
        heightCm: Double,
        defaultWeightKg: Double,
        avatarEmoji: String,
        photoUri: String?
    ) {
        viewModelScope.launch {
            val age = ScaleUtils.calculateAgeFromDob(dob)
            val bmi = ScaleUtils.calculateBmi(defaultWeightKg, heightCm)
            val existing = _editingCharacter.value
            if (existing != null) {
                val updated = existing.copy(
                    name = name,
                    role = role,
                    gender = gender,
                    dob = dob,
                    age = age,
                    heightCm = heightCm,
                    defaultWeightKg = defaultWeightKg,
                    defaultBmi = bmi,
                    avatarEmoji = avatarEmoji,
                    photoUri = photoUri ?: existing.photoUri
                )
                repository.updateCharacter(updated)
                if (_selectedCharacter.value?.id == updated.id) {
                    selectCharacter(updated)
                }
                _statusMessage.value = "Karakter diperbarui!"
            } else {
                val newChar = FamilyCharacter(
                    name = name,
                    role = role,
                    gender = gender,
                    dob = dob,
                    age = age,
                    heightCm = heightCm,
                    defaultWeightKg = defaultWeightKg,
                    defaultBmi = bmi,
                    avatarEmoji = avatarEmoji,
                    photoUri = photoUri
                )
                val id = repository.insertCharacter(newChar)
                selectCharacter(newChar.copy(id = id))
                _statusMessage.value = "Karakter berhasil ditambahkan!"
            }
            closeCharacterDialog()
            delay(2500)
            _statusMessage.value = null
        }
    }

    fun deleteCharacter(character: FamilyCharacter) {
        viewModelScope.launch {
            repository.deleteCharacter(character)
            if (_selectedCharacter.value?.id == character.id) {
                val remaining = characters.value.filter { it.id != character.id }
                if (remaining.isNotEmpty()) {
                    selectCharacter(remaining.first())
                } else {
                    _selectedCharacter.value = null
                }
            }
            _statusMessage.value = "Karakter dihapus"
            delay(2000)
            _statusMessage.value = null
        }
    }

    private fun vibrateLight() {
        try {
            val app = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = app.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                app.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(20)
            }
        } catch (_: Exception) {}
    }

    private fun vibrateSuccess() {
        try {
            val app = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = app.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                app.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 40, 60, 50), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(60)
            }
        } catch (_: Exception) {}
    }
}
