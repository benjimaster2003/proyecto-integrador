package com.example.bugcatyyo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class UsuarioFragment : Fragment() {

    lateinit var txt_correo_info: TextView
    lateinit var txt_nombre_info: TextView
    lateinit var txt_apellido_info: TextView
    lateinit var txt_telefono_info: TextView
    lateinit var txt_direccion_info: TextView

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_usuario, container, false)

        val buttonCerrarSesion: Button = view.findViewById(R.id.btn_regresar)
        buttonCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        val buttonActualizarInfo: Button = view.findViewById(R.id.btn_actualizar_datos)
        buttonActualizarInfo.setOnClickListener{
            actualizarDatos()
        }

        txt_correo_info = view.findViewById(R.id.txt_correo_info)
        txt_nombre_info = view.findViewById(R.id.txt_nombre_info)
        txt_apellido_info = view.findViewById(R.id.txt_apellido_info)
        txt_telefono_info = view.findViewById(R.id.txt_telefono_info)
        txt_direccion_info = view.findViewById(R.id.txt_direccion_info)

        val userId = auth.currentUser?.uid

        if (userId != null) {
            db.collection("usuarios").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        txt_nombre_info.text = document.getString("nombre")
                        txt_apellido_info.text = document.getString("apellidos")
                        txt_telefono_info.text = document.getString("telefono")
                        txt_direccion_info.text = document.getString("direccion")
                        txt_correo_info.text = auth.currentUser?.email.toString()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error al obtener datos", Toast.LENGTH_SHORT)
                        .show()
                }
        }
        return view
    }

    private fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun actualizarDatos(){
        val intent = Intent(requireContext(), EditarUsuario::class.java)

        intent.putExtra("correo", txt_correo_info.text.toString())
        intent.putExtra("nombre", txt_nombre_info.text.toString())
        intent.putExtra("apellido", txt_apellido_info.text.toString())
        intent.putExtra("telefono", txt_telefono_info.text.toString())
        intent.putExtra("direccion", txt_direccion_info.text.toString())

        startActivity(intent)
    }
}
