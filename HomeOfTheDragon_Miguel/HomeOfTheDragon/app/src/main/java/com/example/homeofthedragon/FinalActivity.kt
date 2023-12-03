package com.example.homeofthedragon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class FinalActivity : AppCompatActivity() {

    companion object {
        const val EXIT = "EXIT"
        const val CHOSEN = "CHOSEN"
    }
    private lateinit var tvChosen : TextView
    private lateinit var btnStart : Button
    private lateinit var btnBack : Button
    private lateinit var btnGoOn : Button
    private lateinit var name : String
    private lateinit var chosen : String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        tvChosen = findViewById<TextView>(R.id.tvChosen)
        btnStart = findViewById<Button>(R.id.btnStart)
        btnBack = findViewById<Button>(R.id.btnBack)
        btnGoOn = findViewById<Button>(R.id.btnGoOn)

        name = intent.getStringExtra(MainActivity.NAME)?:""

        chosen = intent.getStringExtra(ElectionActivity.CHOICE)?:""
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

        btnGoOn.setOnClickListener {
            goToScape()
        }
    }

    private fun goToScape() {
        //Creamos el intent e incorporamos el nombre introducido.
        val intent = Intent(this@FinalActivity,ScapeActivity::class.java)
        intent.putExtra(MainActivity.NAME,name)
        intent.putExtra(CHOSEN,chosen)

        startActivity(intent)

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
        System.exit(0)
    }
}