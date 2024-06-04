package com.example.guesstheword.screens.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.guesstheword.dependencies.MyApplication
import com.example.guesstheword.repositories.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoVM(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    //Flujo de estado.
    private val _uiState : MutableStateFlow<PhotoUiState> = MutableStateFlow(PhotoUiState())
    val uiState : StateFlow<PhotoUiState> = _uiState.asStateFlow()

    private var name = ""
    private var level = 0

    fun setPhoto(name : String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    photoName = name
                )
            }
        }
    }

    fun savePhoto(photoName: String) {
        viewModelScope.launch {
            //edita el DataStore.
            userPreferencesRepository.saveSettings(name, level, photoName)
            //actualiza el flujo de estado para que persista en pantalla.
            _uiState.update { currentState ->
                currentState.copy(
                    savedPhoto = true
                )
            }
        }
    }

    fun setNameAndLevel(n0: String, l0: Int) {
        name = n0
        level = l0
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

                return PhotoVM(
                    (application as MyApplication).appcontainer.userPreferencesRepository
                ) as T
            }
        }
    }

}