package com.example.bugcatyyo

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProductosAdapter(
    private var productosList: List<Producto>,
    private val onItemClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenImageView: ImageView = itemView.findViewById(R.id.imageView)
        val nombreTextView: TextView = itemView.findViewById(R.id.catalogo_nombre)
        val precioTextView: TextView = itemView.findViewById(R.id.catalogo_precio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalogo_item, parent, false)
        return ProductoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productosList[position]

        Picasso.get().load(producto.imagen).into(holder.imagenImageView)

        val nombreConFormato = if (producto.nombre.length > 16) {
            "${producto.nombre.substring(0, 13)}..."
        } else {
            producto.nombre
        }
        holder.nombreTextView.text = nombreConFormato
        val precioConFormato = "S/. ${String.format("%.2f", producto.precio)}"
        holder.precioTextView.text = precioConFormato

        holder.itemView.setOnClickListener {
            onItemClick(producto)
        }
    }

    fun actualizarProductos(nuevaLista: List<Producto>) {
        productosList = nuevaLista
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return productosList.size
    }

    class EspaciadoItemDecoration(private val espaciado: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.top = espaciado
            outRect.left = espaciado
            outRect.right = espaciado
            outRect.bottom = espaciado
        }
    }





}
