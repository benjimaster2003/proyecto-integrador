package com.example.bugcatyyo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class AgregarProducto : AppCompatActivity() {

    private lateinit var txtName: TextInputEditText
    private lateinit var txtDescription: TextInputEditText
    private lateinit var txtCategory: TextInputEditText
    private lateinit var txtImage: TextInputEditText
    private lateinit var txtPrice: TextInputEditText
    private lateinit var txtUbicacion: TextInputEditText
    private lateinit var addButton: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto)

        txtName = findViewById(R.id.txt_name)
        txtDescription = findViewById(R.id.txt_description)
        txtCategory = findViewById(R.id.txt_category)
        txtImage = findViewById(R.id.txt_imagen)
        txtPrice = findViewById(R.id.txt_price)
        txtUbicacion = findViewById(R.id.txt_ubicacion)
        addButton = findViewById(R.id.btn_Actualizar)

    }

    fun agregarProducto(view: View) {
        val nombre = txtName.text.toString()
        val descripcion = txtDescription.text.toString()
        val categoria = txtCategory.text.toString()
        val imagen = txtImage.text.toString()
        val precio = txtPrice.text.toString().toDoubleOrNull()
        val ubicacion = txtUbicacion.text.toString()

        if (nombre.isNotEmpty() && descripcion.isNotEmpty() && categoria.isNotEmpty() && imagen.isNotEmpty() && precio != null) {
            val producto = Producto(imagen, nombre, precio, descripcion, categoria, ubicacion)

            db.collection("productos")
                .add(producto)
                .addOnSuccessListener { documentReference ->
                    val nuevoId = documentReference.id

                    producto.id = nuevoId

                    db.collection("productos").document(nuevoId)
                        .set(producto)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registro completado", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MantenimientoActivity::class.java))
                        }

                }
        }
    }


}