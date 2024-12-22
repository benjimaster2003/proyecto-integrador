package com.example.bugcatyyo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroUsuario : AppCompatActivity() {

    private lateinit var txtCorreoRegistro: EditText
    private lateinit var txtContrasenaRegistro: EditText
    private lateinit var txtNombreRegistro: EditText
    private lateinit var txtApellidosRegistro: EditText
    private lateinit var txtTelefonoRegistro: EditText
    private lateinit var txtDireccionRegistro: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        txtCorreoRegistro = findViewById(R.id.txt_correo_registro)
        txtContrasenaRegistro = findViewById(R.id.txt_contrasena_registro)
        txtNombreRegistro = findViewById(R.id.txt_nombre_registro)
        txtApellidosRegistro = findViewById(R.id.txt_apellido_registro)
        txtTelefonoRegistro = findViewById(R.id.txt_telefono_registro)
        txtDireccionRegistro = findViewById(R.id.txt_direccion_registro)

    }

    fun registrarUsuario(view: View) {

        val email = txtCorreoRegistro.text.toString()
        val password = txtContrasenaRegistro.text.toString()
        val nombre = txtNombreRegistro.text.toString()
        val apellidos = txtApellidosRegistro.text.toString()
        val telefono = txtTelefonoRegistro.text.toString()
        val direccion = txtDireccionRegistro.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.uid?.let { uid ->
                        val usuario = hashMapOf(
                            "nombre" to nombre,
                            "apellidos" to apellidos,
                            "telefono" to telefono,
                            "direccion" to direccion
                        )

                        db.collection("usuarios")
                            .document(uid)
                            .set(usuario)
                            .addOnSuccessListener {
                                Toast.makeText(baseContext,"Bugcat Registrado",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, CatalogoActivity::class.java))
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(baseContext, "Error al guardar información: ${e.message}.",Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(baseContext,"Error al registrar usuario.",Toast.LENGTH_SHORT).show()
                }
            }
    }
}