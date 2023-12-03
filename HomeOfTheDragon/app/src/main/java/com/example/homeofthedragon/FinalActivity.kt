package com.example.homeofthedragon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class FinalActivity : AppCompatActivity() {

    companion object {
        const val EXIT = "EXIT"
    }
    private lateinit var tvChosen : TextView
    private lateinit var btnStart : Button
    private lateinit var btnBack : Button
    private lateinit var btnExit : Button
    private lateinit var name : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        tvChosen = findViewById<TextView>(R.id.tvChosen)
        btnStart = findViewById<Button>(R.id.btnStart)
        btnBack = findViewById<Button>(R.id.btnBack)
        btnExit = findViewById<Button>(R.id.btnExit)

        name = intent.getStringExtra(MainActivity.NAME)?:""

        val chosen = intent.getStringExtra(ElectionActivity.CHOICE)
        when(chosen) {
            Choice.NONE.nameElection -> {
                tvChosen.text = getString(R.string.none_chosen)
            }
            Choice.RHAENIRA.nameElection -> {
                tvChosen.text = getString(R.string.rhaenyra_chosen)
            }
            Choice.AEGON.nameElection -> {
                tvChosen.text = getString(R.string.aegon_chosen)
            }
            Choice.BOTH.nameElection -> {
                tvChosen.text = getString(R.string.both_chosen)
            }
        }

        btnStart.setOnClickListener {
            goToMain()
        }

        btnBack.setOnClickListener {
            goToElection()
        }

        btnExit.setOnClickListener {
            exit()
        }
    }

    private fun goToElection() {
        //Creamos el intent e incorporamos el nombre introducido.
        val intent = Intent(this@FinalActivity,ElectionActivity::class.java)
        intent.putExtra(MainActivity.NAME,name)

        startActivity(intent)

        //Cierra la actividad actual
        finish()
    }

    private fun goToMain() {
        val intent = Intent(this@FinalActivity,MainActivity::class.java)
        startActivity(intent)

        //Cierra la actividad actual
        finish()
    }

    private fun exit() {
        finish()
    }
}