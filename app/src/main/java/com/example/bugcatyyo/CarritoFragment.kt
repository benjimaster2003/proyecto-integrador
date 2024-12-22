package com.example.bugcatyyo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CarritoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DetalleProductoAdapter
    private val detalleProductosList = mutableListOf<DetalleProducto>()
    private var montoTotal: Double = 0.0


    //
    private lateinit var btnPagar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)

        adapter = DetalleProductoAdapter(requireContext(), detalleProductosList) {
            eliminarDetalleProducto(it)
        }

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        cargarDetalleProductos()


        ///
        btnPagar = view.findViewById(R.id.btn_Pagar)
        btnPagar.setOnClickListener {
            pagar()
        }

        return view
    }

    private fun cargarDetalleProductos() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = FirebaseFirestore.getInstance()
            val usuarioRef = db.collection("usuarios").document(user.uid)
            val detalleProductosRef = usuarioRef.collection("detalleproductos")

            detalleProductosRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val nuevosDetalleProductosList = mutableListOf<DetalleProducto>()
                    montoTotal = 0.0

                    for (document in querySnapshot.documents) {
                        val detalleProducto = document.toObject(DetalleProducto::class.java)
                        detalleProducto?.let {
                            nuevosDetalleProductosList.add(it)
                            montoTotal += it.subtotal
                        }
                    }

                    detalleProductosList.clear()
                    detalleProductosList.addAll(nuevosDetalleProductosList)
                    adapter.notifyDataSetChanged()

                    if (isAdded) {
                        actualizarMontoTotal()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error al cargar los productos del carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun actualizarMontoTotal() {
        val txtMontoTotal: TextView = requireView().findViewById(R.id.txt_monto_total)
        txtMontoTotal.text = "Total a pagar: S/. $montoTotal    "
    }

    private fun eliminarDetalleProducto(detalleProducto: DetalleProducto) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = FirebaseFirestore.getInstance()
            val usuarioRef = db.collection("usuarios").document(user.uid)
            val detalleProductosRef = usuarioRef.collection("detalleproductos")

            detalleProductosRef.whereEqualTo("nombre", detalleProducto.nombre)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        document.reference.delete()
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    requireContext(),
                                    "Error al eliminar un producto",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }

                    detalleProductosList.removeAll { it.nombre == detalleProducto.nombre }
                    adapter.setDetalleProductosList(detalleProductosList)
                    actualizarMontoTotal()
                    cargarDetalleProductos()

                    Toast.makeText(
                        requireContext(),
                        "Producto eliminado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error al buscar los productos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun pagar() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = FirebaseFirestore.getInstance()
            val ventaRef = db.collection("ventas")

            val fechaActual = obtenerFechaActual()
            val montoTotal = montoTotal.toString()

            val venta = hashMapOf(
                "idUsuario" to user.email,
                "fecha" to fechaActual,
                "montoTotal" to montoTotal
            )

            ventaRef.add(venta)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Compra realizada, ¡Gracias!",
                        Toast.LENGTH_SHORT
                    ).show()
                    eliminarProductosCarrito()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error al realizar la venta: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


    private fun eliminarProductosCarrito() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = FirebaseFirestore.getInstance()
            val usuarioRef = db.collection("usuarios").document(user.uid)
            val detalleProductosRef = usuarioRef.collection("detalleproductos")

            detalleProductosRef.get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        document.reference.delete()
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    requireContext(),
                                    "Error al eliminar productos del carrito",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }

                    detalleProductosList.clear()
                    adapter.setDetalleProductosList(detalleProductosList)
                    actualizarMontoTotal()
                    cargarDetalleProductos()

                    Toast.makeText(
                        requireContext(),
                        "Productos eliminados del carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error al obtener productos del carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun obtenerFechaActual(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaActual = Date()
        return dateFormat.format(fechaActual)
    }

}
