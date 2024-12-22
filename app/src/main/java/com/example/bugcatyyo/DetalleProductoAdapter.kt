package com.example.bugcatyyo

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DetalleProductoAdapter(
    private val context: Context,
    private var items: List<DetalleProducto>,
    private val onDeleteClickListener: (DetalleProducto) -> Unit
) : RecyclerView.Adapter<DetalleProductoAdapter.DetalleProductoViewHolder>() {


    class DetalleProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.txt_nombre_detalleprod)
        val cantidadTextView: TextView = itemView.findViewById(R.id.txt_cantidad_detalleprod)
        val subtotalTextView: TextView = itemView.findViewById(R.id.txt_total_detalleprod)
        val precioTextView: TextView = itemView.findViewById(R.id.txt_precio_detalleprod)
        val imagenImageView: ImageView = itemView.findViewById(R.id.img_imgdetalleprod)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalleProductoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.carrito_item, parent, false)
        return DetalleProductoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DetalleProductoViewHolder, position: Int) {
        val item = items[position]
        holder.nombreTextView.text = item.nombre
        holder.cantidadTextView.text = "Cantidad:\n\n${item.cantidad}"
        holder.cantidadTextView.gravity = Gravity.CENTER
        holder.subtotalTextView.text = "Subtotal:\n\nS/. ${item.subtotal}"
        holder.precioTextView.text = "S/. ${item.precio}"
        holder.precioTextView.gravity=Gravity.CENTER

        Picasso.get().load(item.imagen).into(holder.imagenImageView)

        holder.itemView.findViewById<TextView>(R.id.txt_boton_eliminar)
            .setOnClickListener { onDeleteClickListener.invoke(item) }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setDetalleProductosList(newList: List<DetalleProducto>) {
        items = newList
        notifyDataSetChanged()
    }
}
