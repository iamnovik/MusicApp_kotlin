package com.example.mobproj.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobproj.R
import com.example.mobproj.Repositories.AlbumRepository
import java.io.File

class SlideAdapter(private val images: List<Uri>) : RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_in_slide, parent, false)
        return SlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        //val imageUri = Uri.fromFile(images[position])
        holder.imageView.setImageURI(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
