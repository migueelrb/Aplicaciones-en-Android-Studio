package com.example.homeofthedragon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView

class ElectionActivity : AppCompatActivity() {

    companion object {
        const val CHOICE = "CHOICE"
    }

    private lateinit var tvGreetingElection : TextView
    private lateinit var tvChoice : TextView
    private lateinit var cbRhaenyra : CheckBox
    private lateinit var cbAegon : CheckBox
    private lateinit var btnKneel : Button
    private lateinit var name : String
    //enum class declarado al pie de esta Activity.
    private lateinit var choice : Choice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_election)

        //inicialización de Views.
        tvGreetingElection = findViewById<TextView>(R.id.tvGreetingElection)
        tvChoice = findViewById<TextView>(R.id.tvChoice)
        cbRhaenyra = findViewById<CheckBox>(R.id.cbRhaenyra)
        cbAegon = findViewById<CheckBox>(R.id.cbAegon)
        btnKneel = findViewById<Button>(R.id.btnKneel)
        choice = Choice.NONE

        //tomamos el valor del parámetro String que llega con el intent.
        name = intent.getStringExtra(MainActivity.NAME)?:""
        tvGreetingElection.text = getString(R.string.greeting_election,name)

        cbRhaenyra.setOnCheckedChangeListener { _, b ->
            showChoice(b,cbAegon.isChecked)
        }

        cbAegon.setOnCheckedChangeListener { _, b ->
            showChoice(cbRhaenyra.isChecked,b)
        }

        btnKneel.setOnClickListener {
            goToFinal()
        }

    }

    private fun goToFinal() {
        val intent = Intent(this@ElectionActivity,FinalActivity::class.java)
        intent.putExtra(CHOICE,choice.nameElection)
        intent.putExtra(MainActivity.NAME,name)

        startActivity(intent)

        //Cierra la actividad actual
        finish()
    }

    private fun showChoice(checkedRhaenira: Boolean, checkedAegon: Boolean) {
        when {
            !checkedRhaenira && !checkedAegon -> {
                tvChoice.text = getString(R.string.must_choice)
                choice = Choice.NONE
            }
            checkedRhaenira && !checkedAegon -> {
                tvChoice.text = getString(R.string.rhaenyra_choice)
                choice = Choice.RHAENIRA
            }
            !checkedRhaenira && checkedAegon -> {
                tvChoice.text = getString(R.string.aegon_choice)
                choice = Choice.AEGON
            }
            checkedRhaenira && checkedAegon -> {
                tvChoice.text = getString(R.string.both_choice)
                choice = Choice.BOTH
            }
        }
    }
}

enum class Choice(val nameElection:String) {
    NONE("none"),
    RHAENIRA("Rhaenyra"),
    AEGON("Aegon"),
    BOTH("both")
}