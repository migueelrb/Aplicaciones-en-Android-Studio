package com.example.homeofthedragon

import android.content.res.Resources
import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible

class Castle (
    private var btnList: MutableList<Button>,
    private var player: String,
    private var maxMovements : Int = 7,
    private val resources : Resources,
    private val tvEventCastle : TextView,
    private val btnExit : TextView
)
{
    private var position : Int = (0..2).random()
    private var rooms : Int = 9
    private var hasKey : Boolean = false
    private var movements = 0
    private var endOfGame = false
    private var keyPosition : Int = 0
    private var dragonPosition : Int = 0
    private var exitPosition: Int = 0
    private lateinit var events : Array<String>

    init {
        btnList.forEachIndexed { index, button ->
            button.setOnClickListener {
                goToRoom(index)
            }
        }
        events = resources.getStringArray(R.array.pursuit_events)
        do {
            keyPosition = (0 until rooms).random()
            if(keyPosition != position)
                break
        } while (true)
        do {
            exitPosition = (0 until rooms).random()
            if(exitPosition != position && exitPosition != keyPosition)
                break
        } while (true)
        do {
            dragonPosition = (0 until rooms).random()
            if(dragonPosition != position && dragonPosition != keyPosition && dragonPosition != exitPosition)
                break
        } while (true)
        setRooms()
    }

    private fun setRooms() {
        (0 until rooms).forEach {
            when {
                it == position -> {
                    btnList[it].apply {
                        isEnabled = true
                        text = player
                        isClickable = false
                        setBackgroundColor(resources.getColor(R.color.player))
                    }
                }
                it == exitPosition -> {
                    btnList[it].apply {
                        isEnabled = true
                        setBackgroundColor(Color.RED)
                        text = FinalActivity.EXIT
                        isClickable = isCollindant(exitPosition)
                    }
                }
                isCollindant(it) -> {
                    btnList[it].apply {
                        isEnabled=true
                        isClickable=true
                        text = ""
                        setBackgroundColor(resources.getColor(R.color.collindant))
                    }

                }
                else -> {
                    btnList[it].apply {
                        isEnabled = false
                        text = ""
                        setBackgroundColor(Color.LTGRAY)
                    }
                }
            }
        }
    }

    private fun goToRoom(index: Int) {
        movements++
        position = index
        when(position) {
            keyPosition -> {
                tvEventCastle.text =
                    "Evento $movements/$maxMovements: " + resources.getString(R.string.key_found)
                hasKey = true
            }
            dragonPosition -> {
                tvEventCastle.text =
                    "Evento $movements/$maxMovements: " + resources.getString(R.string.dragon_found)
                endOfGame=true
            }
            exitPosition -> {
                if (hasKey) {
                    tvEventCastle.text =
                        "Evento $movements/$maxMovements: " + resources.getString(R.string.exit_found_key)
                    endOfGame=true
                } else {
                    tvEventCastle.text =
                        "Evento $movements/$maxMovements: " + resources.getString(R.string.exit_found_nokey)
                }
            }
            else -> {
                if (movements >= maxMovements) {
                    tvEventCastle.text =
                        "Evento $movements/$maxMovements: " + resources.getString(R.string.time_out)
                    endOfGame=true
                } else {
                    tvEventCastle.text = "Evento $movements/$maxMovements: " + events.random()
                }
            }
        }
        setRooms()
        if(endOfGame) {
            endOfGame()
        }
    }

    private fun endOfGame() {
        btnList.forEach {
            it.isClickable = false
        }
        btnExit.isVisible = true
    }

    private fun isCollindant(it : Int) : Boolean {
        return (
                (it == (position - 1)) && ((position != 3) && (position != 6)) ||
                        (it == (position + 1)) && ((position != 2) && (position != 5)) ||
                        (it == (position + 3)) || (it == (position - 3))
                )
    }
}