package com.example.homeofthedragon

import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar

class ScapeActivity : AppCompatActivity() {

    private lateinit var tvEvent : TextView
    private val btnListCastle : MutableList<Button> = mutableListOf()
    private lateinit var btnExit : Button
    private lateinit var ivPortrait : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scape)

        tvEvent = findViewById(R.id.tvEventDesc)

        val chosen = intent.getStringExtra(FinalActivity.CHOSEN)
        val name = intent.getStringExtra(MainActivity.NAME)?:""

        ivPortrait = findViewById(R.id.ivPortrait)
        if(chosen == Choice.RHAENIRA.nameElection) {
            ivPortrait.setImageResource(R.drawable.aegon)
            tvEvent.text = getString(R.string.can_scape,Choice.AEGON.nameElection)
        } else {
            tvEvent.text = getString(R.string.can_scape,Choice.RHAENIRA.nameElection)
        }

        (0..8).forEach {
            btnListCastle.add(findViewById<Button>(resources.getIdentifier("btnRoom$it", "id",packageName)))
        }

        btnListCastle.forEach {
            it.isEnabled = false
        }

        btnExit = findViewById(R.id.btnExit)
        btnExit.setOnClickListener {
            exitGame()
        }

        val castle = Castle(btnList = btnListCastle, player = name, resources = resources, tvEventCastle = tvEvent, btnExit = btnExit)
        System.out.println(castle)
    }

    private fun exitGame() {
        finishAffinity()
        System.exit(0)
    }

}
