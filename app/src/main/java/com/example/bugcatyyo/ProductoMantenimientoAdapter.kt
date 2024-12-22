package com.example.bugcatyyo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class ProductoMantenimientoAdapter(context: Context, productos: List<Producto>) :
    ArrayAdapter<Producto>(context, 0, productos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.catalogo_listado_mantenimiento, parent, false)
        }

        val producto = getItem(position)
        val nombreTextView: TextView = view!!.findViewById(R.id.txt_nombre_producto_mant)
        val descripcionTextView: TextView = view.findViewById(R.id.txt_descripcion_producto_mant)
        val imagenImageView: ImageView = view.findViewById(R.id.imagen_producto_mant)

        nombreTextView.text = producto!!.nombre
        descripcionTextView.text = producto.descripcion
        Picasso.get().load(producto.imagen).into(imagenImageView)

        view.setOnClickListener {
            val intent = Intent(context, EditarProducto::class.java)

            intent.putExtra("id", producto.id)
            intent.putExtra("nombre", producto.nombre)
            intent.putExtra("descripcion", producto.descripcion)
            intent.putExtra("categoria", producto.categoria)
            intent.putExtra("precio", producto.precio)
            intent.putExtra("imagen", producto.imagen)

            context.startActivity(intent)
        }

        return view
    }
}




