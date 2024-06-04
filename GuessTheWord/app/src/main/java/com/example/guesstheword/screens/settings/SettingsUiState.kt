package com.example.guesstheword.screens.settings

import com.example.guesstheword.datamodel.UserPreferences

data class SettingsUiState(
    val name : String = "",
    val level : Int = UserPreferences.MEDIUM,
    val photo : String = "",
    val savedSettings : Boolean = false
)