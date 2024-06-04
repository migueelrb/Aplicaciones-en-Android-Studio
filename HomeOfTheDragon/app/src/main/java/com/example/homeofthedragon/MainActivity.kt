package com.example.homeofthedragon

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    //declaración de constantes
    companion object {
        const val NAME = "NAME"
        const val MAIN = "Main"
        const val LIFECLICLE = "LifeCycle"
    }

    //declaración de views.
    private lateinit var btnAnswer : Button
    private lateinit var etName : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //inicialización de views.
        btnAnswer = findViewById<Button>(R.id.btnAnswer)
        etName = findViewById<EditText>(R.id.etName)

        //Evento de escucha del botón.
        // Se toma el valor del cuadro de texto y se pasa a la función que abre la nueva activity.
        btnAnswer.setOnClickListener {
            val name = etName.text.toString()
            Log.d(MAIN,"Botón pulsado. El nombre introducido es $name")
            hideKeyboard(it)
            if(name=="")
                Snackbar.make(it, getString(R.string.say_your_name),Snackbar.LENGTH_SHORT).show()
            else
                goToElection(name)
        }
    }

    private fun goToElection(name : String) {
        //Creamos el intent e incorporamos el nombre introducido.
        val intent = Intent(this@MainActivity,ElectionActivity::class.java)
        intent.putExtra(NAME,name)

        startActivity(intent)

        //Cierra la actividad actual
        finish()
    }

    private fun hideKeyboard(it: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(LIFECLICLE,getString(R.string.activity_restarted))
    }

    override fun onStart() {
        super.onStart()
        Log.d(LIFECLICLE,getString(R.string.activity_started))
    }

    override fun onResume() {
        super.onResume()
        Log.d(LIFECLICLE,getString(R.string.activity_resumed))
    }

    override fun onPause() {
        super.onPause()
        Log.d(LIFECLICLE,getString(R.string.activity_paused))
    }

    override fun onStop() {
        super.onStop()
        Log.d(LIFECLICLE,getString(R.string.activity_stopped))
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LIFECLICLE,getString(R.string.activity_destroyed))
    }

}

