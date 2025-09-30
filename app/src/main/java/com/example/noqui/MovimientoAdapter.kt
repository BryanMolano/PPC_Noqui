package com.example.noqui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovimientoAdapter(
    private val movimientos: List<Movimiento>
) : RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder>() {

    class MovimientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMovimiento: TextView = itemView.findViewById(R.id.txtMovimiento)
        val txtMonto: TextView = itemView.findViewById(R.id.txtMonto)
        val txtPD: TextView = itemView.findViewById(R.id.txtPD)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movimiento, parent, false)
        return MovimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val movimiento = movimientos[position]
        holder.txtMovimiento.text = movimiento.servicio
        holder.txtMonto.text = "$${movimiento.monto}"
        holder.txtPD.text = "Para"
    }

    override fun getItemCount(): Int = movimientos.size
}
