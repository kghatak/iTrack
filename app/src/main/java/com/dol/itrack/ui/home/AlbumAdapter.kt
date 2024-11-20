package com.dol.itrack.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dol.itrack.R
import com.dol.itrack.databinding.AlbumItemBinding

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        // Set dummy image based on position
        val imageResource = if (position % 2 == 0) {
            R.drawable.placeholder_image// Replace with your actual image resource
        } else {
            R.drawable.placeholder_image // Replace with your actual image resource
        }
        holder.binding.albumImageView.setImageResource(imageResource)
    }

    override fun getItemCount(): Int = 10 // Two dummy items

    inner class AlbumViewHolder(val binding: AlbumItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}