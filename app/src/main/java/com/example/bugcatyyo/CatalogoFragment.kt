package com.example.bugcatyyo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class CatalogoFragment : Fragment() {

    private lateinit var productosAdapter: ProductosAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalogo, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        productosAdapter = ProductosAdapter(emptyList()) { producto ->
            abrirDetalleProductoFragment(producto)
        }
        recyclerView.adapter = productosAdapter

        val espaciadoItemDecoration = ProductosAdapter.EspaciadoItemDecoration(8)
        recyclerView.addItemDecoration(espaciadoItemDecoration)

        obtenerProductosDesdeFirestore()

        val ropaButton: Button = view.findViewById(R.id.btn_Ropa)
        val peluchesButton: Button = view.findViewById(R.id.btnPeluches)
        val tazasButton: Button = view.findViewById(R.id.btnTazas)
        val stickersButton: Button = view.findViewById(R.id.btnAccesorios)

        ropaButton.setOnClickListener { mostrarListadoRopa() }
        peluchesButton.setOnClickListener { mostrarListadoPeluches() }
        tazasButton.setOnClickListener { mostrarListadoTazas() }
        stickersButton.setOnClickListener { mostrarListadoStickers() }

        val buscador: SearchView = view.findViewById(R.id.buscador)

        buscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { buscarProductosPorNombre(it) }
                return true
            }
        })

        return view
    }

    private fun obtenerProductosDesdeFirestore() {
        val productosList = mutableListOf<Producto>()

        val db = FirebaseFirestore.getInstance()
        val productosRef = db.collection("productos")

        productosRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val imagen = document.getString("imagen") ?: ""
                    val nombre = document.getString("nombre") ?: ""
                    val precio = document.getDouble("precio") ?: 0.0
                    val descripcion = document.getString("descripcion") ?: ""
                    val ubicacion = document.getString("ubicacion") ?: ""

                    val producto = Producto(imagen, nombre, precio, descripcion, "", ubicacion)
                    productosList.add(producto)
                }

                productosAdapter.actualizarProductos(productosList)
            }
    }

    private fun mostrarListadoCategoria(categoria: String) {
        val db = FirebaseFirestore.getInstance()
        val productosRef = db.collection("productos")
            .whereEqualTo("categoria", categoria)

        productosRef.get()
            .addOnSuccessListener { result ->
                val productosList = mutableListOf<Producto>()

                for (document in result) {
                    val imagen = document.getString("imagen") ?: ""
                    val nombre = document.getString("nombre") ?: ""
                    val precio = document.getDouble("precio") ?: 0.0
                    val descripcion = document.getString("descripcion") ?: ""
                    val ubicacion = document.getString("ubicacion") ?: ""

                    val producto = Producto(imagen, nombre, precio, descripcion, "", ubicacion)
                    productosList.add(producto)
                }

                productosAdapter.actualizarProductos(productosList)
            }
    }

    private fun mostrarListadoRopa() {
        mostrarListadoCategoria("Ropa")
    }

    private fun mostrarListadoPeluches() {
        mostrarListadoCategoria("Peluche")
    }

    private fun mostrarListadoTazas() {
        mostrarListadoCategoria("Taza")
    }

    private fun mostrarListadoStickers() {
        mostrarListadoCategoria("Accesorio")
    }

    private fun buscarProductosPorNombre(query: String) {
        val db = FirebaseFirestore.getInstance()
        val productosRef = db.collection("productos")

        productosRef.get()
            .addOnSuccessListener { result ->
                val productosList = mutableListOf<Producto>()

                for (document in result) {
                    val nombre = document.getString("nombre") ?: ""
                    val nombreLower = nombre.toLowerCase(Locale.getDefault())

                    if (nombreLower.contains(query.toLowerCase(Locale.getDefault()))) {
                        val imagen = document.getString("imagen") ?: ""
                        val precio = document.getDouble("precio") ?: 0.0
                        val descripcion = document.getString("descripcion") ?: ""
                        val ubicacion = document.getString("ubicacion") ?: ""

                        val producto = Producto(imagen, nombre, precio, descripcion, "", ubicacion)
                        productosList.add(producto)
                    }
                }
                productosAdapter.actualizarProductos(productosList)
            }
    }

    private fun abrirDetalleProductoFragment(producto: Producto) {
        Log.d("CatalogoFragment", "Abriendo detalle para producto con ubicación: ${producto.ubicacion}")
        val detalleProductoFragment = DetalleProductoFragment().apply {
            arguments = Bundle().apply {
                putString("descripcion", producto.descripcion)
                putString("imagen", producto.imagen)
                putString("nombre", producto.nombre)
                putDouble("precio", producto.precio)
                putString("ubicacion", producto.ubicacion) // Pasar ubicacion
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detalleProductoFragment)
            .addToBackStack(null)
            .commit()
    }
}
