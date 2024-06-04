package com.example.guesstheword.screens.settings

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.util.query
import com.example.guesstheword.datamodel.UserPreferences
import com.example.guesstheword.dependencies.*
import com.example.guesstheword.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsVM(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState : MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState())
    val uiState : StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        //al iniciar toma el estado desde el DataStore.
        viewModelScope.launch {
            updateState(false)
        }
    }

    //actualiza el flujo de estado con los datos de DataStore.
    private suspend fun updateState(savedSettings : Boolean) {
        userPreferencesRepository.getUserPrefs().collect { userPrefFlow ->
            _uiState.update {
                it.copy(
                    name = userPrefFlow.name,
                    level = userPrefFlow.level,
                    photo = userPrefFlow.photo,
                    savedSettings = savedSettings
                )
            }
        }
    }

    //borra la foto del estado.
    fun delPicture() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    photo = ""
                )
            }
        }
    }

    //guarda la configuración en el datastore.
    fun saveSettings(name: String, level : Int) {
        viewModelScope.launch {
            //edita el DataStore.
            userPreferencesRepository.saveSettings(name,level,uiState.value.photo)
            //actualiza el flujo de estado para indicar que se han guardado los cambios.
            updateState(true)
        }
    }

    fun savedSettingsCompleted() {
        viewModelScope.launch{
            _uiState.update {
                it.copy(
                    savedSettings = false
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return SettingsVM(
                    (application as MyApplication).appcontainer.userPreferencesRepository
                ) as T
            }
        }
    }
}