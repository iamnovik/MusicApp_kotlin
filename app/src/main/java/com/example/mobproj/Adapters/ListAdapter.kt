package com.example.mobproj.Adapters

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobproj.Album
import com.example.mobproj.R
import com.example.mobproj.Repositories.AlbumRepository
import com.example.mobproj.Repositories.UserRepository

class ListAdapter (val items:List<Album>, val context: Context):RecyclerView.Adapter<ListAdapter.MyViewHolder>(){

    var onItemClick:((Album) -> Unit)? = null;
    var likes = arrayListOf<String>()
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image: ImageView = view.findViewById(R.id.albumlist_imageView);
        val title: TextView = view.findViewById(R.id.albumslist_title_textView);
        val artist: TextView = view.findViewById(R.id.albumlist_artist_textView);
        val year: TextView = view.findViewById(R.id.albumslist_year_textView)
        var button: Button = view.findViewById(R.id.button3)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_in_list,parent,false);

        return MyViewHolder(view);
    }

    override fun getItemCount(): Int {
        return items.count();
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int){

        holder.title.text = items[position].title;
        holder.title.setShadowLayer(20f,2f,2f, Color.parseColor("#000000"))
        holder.artist.text = items[position].artist;
        holder.artist.setShadowLayer(20f,2f,2f, Color.parseColor("#000000"))
        holder.year.text = items[position].year.toString();
        holder.year.setShadowLayer(20f,2f,2f, Color.parseColor("#000000"))
        AlbumRepository.getAlbumImage(
            imagePath = items[position].image.toString() +"/preview.jpg",
            onSuccess = { file ->
                val imageUri = Uri.fromFile(file)
                holder.image.setImageURI(imageUri)

            },
            onFailure = { errorMessage ->
                // Обработка ошибки, если не удалось получить изображение альбома
                println("Failed to fetch album image: $errorMessage")
            }
        )
        val auth = UserRepository.getAuth()
        val user = auth.currentUser?.uid
        var inFavourite : Boolean = false
        var userdata:Map<String,Any>?

        if (user != null) {
            Log.w("BIND","HELLLO")
            AlbumRepository.getFirestore().collection("users").document(user)
                .get()
                .addOnSuccessListener { result ->
                    userdata = result.data
                    likes = (result.data?.get("inFavourites") as ArrayList<String>)
                    if (likes.contains(items[position].id.toString())){
                        inFavourite = true
                        holder.button.setBackgroundResource(R.drawable.favourite_yes)
                    }else{
                        inFavourite = false
                        holder.button.setBackgroundResource(R.drawable.favourite_no)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        }



        holder.itemView.setOnClickListener(){
            onItemClick?.invoke(items[position]);
        }
        holder.button.setOnClickListener(){
            inFavourite = !inFavourite
            if (inFavourite){
                holder.button.setBackgroundResource(R.drawable.favourite_yes)
                if (!likes.contains(items[position].id)){
                    likes.add(items[position].id.toString())

                }
            }else{
                holder.button.setBackgroundResource(R.drawable.favourite_no)
                if (likes.contains(items[position].id.toString())){
                    likes.removeIf{it == items[position].id.toString()}
                }
            }
            if (user != null){
                AlbumRepository.getFirestore().collection("users").document(user)
                    .update("inFavourites",likes)
            }

        }
    }

}