package com.example.bugcatyyo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity

//
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var txtEmail: TextInputLayout
    private lateinit var txtPassword: TextInputLayout
    private lateinit var auth: FirebaseAuth

    lateinit var mediaPlayer: MediaPlayer

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            Toast.makeText(this, "Bievenido " +currentUser.email, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, CatalogoActivity::class.java))
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtEmail = findViewById(R.id.txt_correo_login)
        txtPassword = findViewById(R.id.txt_pass_login)
        auth = Firebase.auth

        mediaPlayer = MediaPlayer.create(this,R.raw.capooiconicsound)
        val serviceIntent = Intent(this, CatalogoActivity::class.java)
        startService(serviceIntent)

    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(this, MusicaService::class.java)
        stopService(serviceIntent)
    }

    fun reproducir(view: View){
        mediaPlayer.start()
    }

    fun login(view: View) {
        val email = txtEmail.editText?.text.toString()
        val password = txtPassword.editText?.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser

                // Un bugcat admin que pueda administrar el negocio
                if (user?.email == "bugcatadmin@gmail.com") {
                    startActivity(Intent(this, MantenimientoActivity::class.java))
                // Para los demás usuarios
                } else {
                    startActivity(Intent(this, CatalogoActivity::class.java))
                    Toast.makeText(this,"Bienvenido",Toast.LENGTH_SHORT)
                }
                finish()
                reproducir(view)
            } else {
                Toast.makeText(
                    baseContext,
                    "Error en la autentificación, verifique los datos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun Registrar(view: View){
        startActivity(Intent(this,RegistroUsuario::class.java))
        reproducir(view)
    }


}