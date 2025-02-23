package com.example.gudanglivestock2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BarangAdapter(private val barangList: List<Barang>) :
    RecyclerView.Adapter<BarangAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaBarang: TextView = itemView.findViewById(R.id.tvnamabarang)
        val jumlahBarang: TextView = itemView.findViewById(R.id.tvjumlah)
        val satuanBarang: TextView = itemView.findViewById(R.id.tvsatuan) // Tambahkan satuan
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.barang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val barang = barangList[position]
        holder.namaBarang.text = barang.namabarang
        holder.jumlahBarang.text = barang.jumlah
        holder.satuanBarang.text = barang.satuan
    }


    override fun getItemCount(): Int = barangList.size

}
