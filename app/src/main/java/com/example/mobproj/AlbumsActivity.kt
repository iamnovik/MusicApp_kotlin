package com.example.mobproj

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobproj.Adapters.ListAdapter
import com.example.mobproj.Repositories.AlbumRepository
import java.util.Calendar

class AlbumsActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        setContentView(R.layout.activity_albums);
        var albums = mutableListOf<Album>()
        val albumsList: RecyclerView = findViewById(R.id.itemslist);
        albumsList.layoutManager = LinearLayoutManager(this);
        var albumAdapter:ListAdapter
        AlbumRepository.getFirestore().collection("albums")
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    val data = document.data
                    albums.add(Album(document.id,data["title"].toString(),data["title"].toString(),data["artist"].toString(),data["year"] as Long))

                }
                albumAdapter = ListAdapter(albums,this)
                albumsList.adapter = albumAdapter
                albumAdapter.onItemClick = {
                    var intent = Intent(this,AlbumProfileActivity::class.java)
                    intent.putExtra("album",it)
                    startActivity(intent)

                }

            }
            .addOnFailureListener { exception ->
                Log.w("ALBUMS", "Error getting documents.", exception)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums);
        var albums = mutableListOf<Album>()
        val albumsList: RecyclerView = findViewById(R.id.itemslist);
        albumsList.layoutManager = LinearLayoutManager(this);
        var albumAdapter:ListAdapter
        AlbumRepository.getFirestore().collection("albums")
            .get()
            .addOnSuccessListener { result ->
                for(document in result){
                    val data = document.data
                    albums.add(Album(document.id,data["title"].toString(),data["title"].toString(),data["artist"].toString(),data["year"] as Long))

                }
                albumAdapter = ListAdapter(albums,this)
                albumsList.adapter = albumAdapter
                albumAdapter.onItemClick = {
                    var intent = Intent(this,AlbumProfileActivity::class.java)
                    intent.putExtra("album",it)
                    startActivity(intent)

                }

            }
            .addOnFailureListener { exception ->
                Log.w("ALBUMS", "Error getting documents.", exception)
            }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.favourites -> {
                val intent = Intent(this, FavouritesActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.albums -> {
                val intent = Intent(this, AlbumsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }


}
