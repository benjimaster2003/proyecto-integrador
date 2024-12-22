package com.example.bugcatyyo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class DetalleProductoFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_detalle_producto, container, false)

        val descripcion = arguments?.getString("descripcion")
        val imagen = arguments?.getString("imagen")
        val nombre = arguments?.getString("nombre")
        val precio = arguments?.getDouble("precio")
        val ubicacion = arguments?.getString("ubicacion") // obtener la ubicación

        Log.d("DetalleProductoFragment", "Ubicación en onCreateView: $ubicacion")

        view.findViewById<TextView>(R.id.txt_nombre_producto).text = nombre
        view.findViewById<TextView>(R.id.txt_descripcion_producto).text = descripcion
        val precioFormateado = String.format("S/. %.2f", precio)
        view.findViewById<TextView>(R.id.txt_precio_producto).text = precioFormateado

        Picasso.get().load(imagen).into(view.findViewById<ImageView>(R.id.imagen_producto))

        view.findViewById<Button>(R.id.btn_comprar).setOnClickListener {
            agregarDetalleProductoAlCarrito()
        }

        // Para Aumentar o disminuir
        val txtCantidad = view.findViewById<EditText>(R.id.txt_cantidad)

        view.findViewById<Button>(R.id.btn_aumentar).setOnClickListener {
            incrementarCantidad(txtCantidad)
        }

        view.findViewById<Button>(R.id.btn_disminuir).setOnClickListener {
            disminuirCantidad(txtCantidad)
        }

        val imgRegresar = view.findViewById<ImageView>(R.id.img_regresar)

        imgRegresar.setOnClickListener {
            val catalogoFragment = CatalogoFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, catalogoFragment)
                .addToBackStack(null)
                .commit()
        }

        // Obtener referencia del MapView y inicializarlo
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Obtener la ubicación de los argumentos y agregar un marcador
        val ubicacion = arguments?.getString("ubicacion") ?: return
        Log.d("DetalleProductoFragment", "Ubicación recibida en onMapReady: $ubicacion")
        if (ubicacion.isNotBlank()) {
            val coordinates = ubicacion.split(",")
            if (coordinates.size == 2) {
                try {
                    val latLng = LatLng(coordinates[0].toDouble(), coordinates[1].toDouble())
                    Log.d("DetalleProductoFragment", "Coordenadas convertidas: $latLng")
                    googleMap?.addMarker(MarkerOptions().position(latLng).title("Ubicación del Producto"))
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                } catch (e: NumberFormatException) {
                    Log.e("DetalleProductoFragment", "Error al convertir coordenadas: ${e.message}")
                }
            } else {
                Log.e("DetalleProductoFragment", "Coordenadas no válidas: $coordinates")
            }
        } else {
            Log.e("DetalleProductoFragment", "Ubicación vacía o no válida")
        }
    }

    // Otros métodos de ciclo de vida del MapView
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun agregarDetalleProductoAlCarrito() {
        val cantidadEditText = view?.findViewById<EditText>(R.id.txt_cantidad)
        val cantidadString = cantidadEditText?.text?.toString() ?: "0"

        if (cantidadString.isNotEmpty()) {
            val cantidad = cantidadString.toInt()
            val precio = arguments?.getDouble("precio") ?: 0.0
            val subtotal = cantidad * precio

            val detalleProducto = hashMapOf(
                "cantidad" to cantidad,
                "imagen" to (arguments?.getString("imagen") ?: ""),
                "nombre" to (arguments?.getString("nombre") ?: ""),
                "precio" to precio,
                "subtotal" to subtotal,
                "ubicacion" to (arguments?.getString("ubicacion") ?: "") // agregar ubicacion
            )

            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.let { user ->
                val db = FirebaseFirestore.getInstance()
                val usuarioRef = db.collection("usuarios").document(user.uid)
                val detalleProductosRef = usuarioRef.collection("detalleproductos")

                detalleProductosRef.add(detalleProducto)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(
                            requireContext(),
                            "Producto añadido al carrito con ID: ${documentReference.id}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error al agregar el producto al carrito", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Toast.makeText(requireContext(), "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show()
        }
    }

    private fun incrementarCantidad(txtCantidad: EditText?) {
        var cantidad = txtCantidad?.text?.toString()?.toIntOrNull() ?: 0
        cantidad++
        txtCantidad?.setText(cantidad.toString())
    }

    private fun disminuirCantidad(txtCantidad: EditText?) {
        var cantidad = txtCantidad?.text?.toString()?.toIntOrNull() ?: 0
        cantidad = maxOf(0, cantidad - 1) // Para asegurarse de que no sea menor que 0
        txtCantidad?.setText(cantidad.toString())
    }
}
