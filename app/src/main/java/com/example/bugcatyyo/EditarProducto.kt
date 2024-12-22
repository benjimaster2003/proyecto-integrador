package com.example.bugcatyyo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditarProducto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        val id = intent.getStringExtra("id") ?: ""
        val nombre = intent.getStringExtra("nombre")
        val descripcion = intent.getStringExtra("descripcion")
        val categoria = intent.getStringExtra("categoria")
        val precio = intent.getDoubleExtra("precio", 0.0)
        val imagen = intent.getStringExtra("imagen")

        val txtNombre = findViewById<TextInputEditText>(R.id.txt_name)
        val txtDescripcion = findViewById<TextInputEditText>(R.id.txt_description)
        val txtCategoria = findViewById<TextInputEditText>(R.id.txt_category)
        val txtPrecio = findViewById<TextInputEditText>(R.id.txt_price)
        val txtImagen = findViewById<TextInputEditText>(R.id.txt_imagen)
        val txtId = findViewById<TextView>(R.id.txt_id)

        txtId.text = id
        txtNombre.setText(nombre)
        txtDescripcion.setText(descripcion)
        txtCategoria.setText(categoria)
        txtPrecio.setText(precio.toString())
        txtImagen.setText(imagen)


        //Eliminar
        val btnEliminar = findViewById<Button>(R.id.btn_Eliminar)
        btnEliminar.setOnClickListener {
            mostrarConfirmacionEliminar()
        }

    }

    fun actualizarProducto(view: View) {
        val nombre = findViewById<TextInputEditText>(R.id.txt_name).text.toString()
        val descripcion = findViewById<TextInputEditText>(R.id.txt_description).text.toString()
        val categoria = findViewById<TextInputEditText>(R.id.txt_category).text.toString()
        val precio = findViewById<TextInputEditText>(R.id.txt_price).text.toString().toDoubleOrNull() ?: 0.0
        val imagen = findViewById<TextInputEditText>(R.id.txt_imagen).text.toString()

        val db = FirebaseFirestore.getInstance()
        val productosRef = db.collection("productos")

        val datosActualizados = mapOf(
            "nombre" to nombre,
            "descripcion" to descripcion,
            "categoria" to categoria,
            "precio" to precio,
            "imagen" to imagen
        )

        val idProducto = intent.getStringExtra("id") ?: ""

        productosRef.document(idProducto)
            .update(datosActualizados)
            .addOnSuccessListener {
                Toast.makeText(this, "Producto Actualizado correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MantenimientoActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarConfirmacionEliminar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar eliminación")
        builder.setMessage("¿Estás seguro de que deseas eliminar este producto?")

        builder.setPositiveButton("Sí") { _, _ ->
            eliminarProducto()
        }

        builder.setNegativeButton("No") { _, _ ->
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun eliminarProducto() {
        val id = intent.getStringExtra("id") ?: ""

        val db = FirebaseFirestore.getInstance()
        val productosRef = db.collection("productos")

        productosRef.document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MantenimientoActivity::class.java)
                startActivity(intent)

                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
            }
    }


}
