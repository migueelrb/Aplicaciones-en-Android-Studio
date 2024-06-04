package com.example.guesstheword.screens.game

import com.example.guesstheword.datamodel.Word

data class GameUiState(
    val word : Word? = null,
    val score : Int = 0,
    val wordList : List<Word> = emptyList(),
    val rightWords : List<String> = emptyList(),
    val wrongWords : List<String> = emptyList(),
    val time : Float = 0f,
    val message : String? = null,
    val noMoreWords : Boolean = false,
    val userName : String = "",
    val level : Int = 1,
    val num_word : Int = 1
)