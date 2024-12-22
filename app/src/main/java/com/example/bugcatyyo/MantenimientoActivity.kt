package com.example.bugcatyyo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class MantenimientoActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var productoAdapter: ProductoMantenimientoAdapter
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mantenimiento)

        val detenerMusicaIntent = Intent(this, MusicaService::class.java)
        stopService(detenerMusicaIntent)


        productoAdapter = ProductoMantenimientoAdapter(this, ArrayList())

        listView = findViewById(R.id.listview)
        listView.adapter = productoAdapter

        obtenerProductosDesdeFirestore()

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, EditarProducto::class.java)
            startActivity(intent)
        }

        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

    }

    private fun obtenerProductosDesdeFirestore() {
        val productosList = mutableListOf<Producto>()

        val db = FirebaseFirestore.getInstance()
        val productosRef = db.collection("productos")

        productosRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.getString("id") ?: ""

                    val imagen = document.getString("imagen") ?: ""
                    val nombre = document.getString("nombre") ?: ""
                    val precio = document.getDouble("precio") ?: 0.0
                    val descripcion = document.getString("descripcion") ?: ""
                    val categoria = document.getString("categoria") ?: ""

                    val producto = Producto(id, imagen, nombre, precio, descripcion, categoria)
                    productosList.add(producto)
                }


                productoAdapter = ProductoMantenimientoAdapter(this, productosList)
                listView.adapter = productoAdapter
            }
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
        MantenimientoActivity().finish()
    }


    fun agregarProducto(view:View){
        startActivity(Intent(this,AgregarProducto()::class.java))

    }

}
