package com.example.bugcatyyo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditarUsuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_usuario)

        val correo = intent.getStringExtra("correo")
        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val telefono = intent.getStringExtra("telefono")
        val direccion = intent.getStringExtra("direccion")

        findViewById<EditText>(R.id.txt_nombre_edit).hint = nombre
        findViewById<EditText>(R.id.txt_apellido_edit).hint = apellido
        findViewById<EditText>(R.id.txt_telefono_edit).hint = telefono
        findViewById<EditText>(R.id.txt_direccion_edit).hint = direccion

        val correoTextView = findViewById<TextView>(R.id.txt_correo_info)
        correoTextView.text = correo
        correoTextView.setTextColor(resources.getColor(R.color.gray))


        val btnActualizar = findViewById<Button>(R.id.btn_actualizar_datos)
        btnActualizar.setOnClickListener {
            actualizarDatosUsuario()
        }


    }

    private fun actualizarDatosUsuario() {
        val nuevoNombre = obtenerTextoOHint(findViewById(R.id.txt_nombre_edit))
        val nuevoApellido = obtenerTextoOHint(findViewById(R.id.txt_apellido_edit))
        val nuevoTelefono = obtenerTextoOHint(findViewById(R.id.txt_telefono_edit))
        val nuevaDireccion = obtenerTextoOHint(findViewById(R.id.txt_direccion_edit))

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val usuarioRef = FirebaseFirestore.getInstance().collection("usuarios").document(userId)

            val actualizacionDatos = mapOf(
                "nombre" to nuevoNombre,
                "apellidos" to nuevoApellido,
                "telefono" to nuevoTelefono,
                "direccion" to nuevaDireccion
            )

            usuarioRef.update(actualizacionDatos)
                .addOnSuccessListener {
                    Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                    val fragmentManager = supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, UsuarioFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar : ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun obtenerTextoOHint(editText: EditText): String {
        val texto = editText.text.toString().trim()
        return if (texto.isNotEmpty()) texto else editText.hint.toString()
    }

    fun RegresarMain(view: View){
        startActivity(Intent(this,CatalogoActivity::class.java))
    }


}