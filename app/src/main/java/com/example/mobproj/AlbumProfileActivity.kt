package com.example.mobproj

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.graphics.Color
import android.net.Uri
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.tooling.preview.Preview
import androidx.viewpager2.widget.ViewPager2
import com.example.mobproj.Adapters.SlideAdapter
import com.example.mobproj.Repositories.AlbumRepository
import com.example.mobproj.Repositories.UserRepository
import java.io.File

class AlbumProfileActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val album = intent.getParcelableExtra<Album>("album");
        if (album != null){
            val image: ImageView = findViewById(R.id.album_imageView);
            val title: TextView = findViewById(R.id.album_title_textView);
            val artist: TextView = findViewById(R.id.album_artist_textView);
            val year: TextView = findViewById(R.id.album_year_textView);
            val viewpager : ViewPager2 = findViewById(R.id.album_viewPager);
            val button: Button = findViewById(R.id.add_favourite_button);
            var images = mutableListOf<Uri>()
            var links = mutableListOf<String>()
            for (i in 1..7){
                links.add(album.image.toString() +"/" + i.toString() + ".jpg")

            }
            loadImages(
                links,
                onSuccess = { images ->
                    // Обработка успешной загрузки изображений
                    // images - список Uri загруженных изображений
                    // Например, обновление RecyclerView с новыми изображениями
                    viewpager.adapter = SlideAdapter(images)
                },
                onFailure = { errorMessage ->
                    // Обработка ошибки загрузки изображений
                    println("Failed to load images: $errorMessage")
                }
            )

            title.text = album.title;
            artist.text = album.artist;
            year.text = album.year.toString();
            AlbumRepository.getAlbumImage(
                imagePath = album.image.toString() +"/preview.jpg",
                onSuccess = { file ->
                    val imageUri = Uri.fromFile(file)
                    image.setImageURI(imageUri)

                },
                onFailure = { errorMessage ->
                    // Обработка ошибки, если не удалось получить изображение альбома
                    println("Failed to fetch album image: $errorMessage")
                }
            )

            val auth = UserRepository.getAuth()
            val user = auth.currentUser?.uid
            var likes = arrayListOf<String>()
            if (user != null) {
                AlbumRepository.getFirestore().collection("users").document(user)
                    .get()
                    .addOnSuccessListener { result ->
                        likes = (result.data?.get("inFavourites") as ArrayList<String>)
                        if (likes.contains(album.id.toString())){
                            button.setBackgroundColor(Color.parseColor("#B3EF77"))
                            button.text = "Убрать из избранного"
                        }else{
                            button.setBackgroundColor(Color.parseColor("#EAB2B2"))
                            button.text = "Добавить в избранное"
                        }
                    }
                    .addOnFailureListener { exception ->

                    }
            }


            button.setOnClickListener{
                if (!likes.contains(album.id.toString())){
                    likes.add(album.id.toString())
                    button.setBackgroundColor(Color.parseColor("#B3EF77"))
                    button.text = "Убрать из избранного"
                }
                else{
                    likes.removeIf{it == album.id.toString()}
                    button.setBackgroundColor(Color.parseColor("#EAB2B2"))
                    button.text = "Добавить в избранное"
                }
                if (user != null){
                    AlbumRepository.getFirestore().collection("users").document(user)
                        .update("inFavourites",likes)
                }
            }

        }
    }

    fun loadImages(links: List<String>, onSuccess: (List<Uri>) -> Unit, onFailure: (String) -> Unit) {
        val images = mutableListOf<Uri>()
        var counter = 0
        links.forEach { link ->
            AlbumRepository.getAlbumImage(
                imagePath = link,
                onSuccess = { file ->
                    val imageUri = Uri.fromFile(file)
                    images.add(imageUri)
                    counter++
                    // Если все изображения загружены, вызываем onSuccess с полным списком изображений
                    if (counter == links.size) {
                        onSuccess(images)
                    }
                },
                onFailure = { errorMessage ->
                    counter++
                    if (counter == links.size) {
                        onSuccess(images)
                    }
                }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            val intent = Intent(this,AlbumsActivity::class.java)
            startActivity(intent)
            finish()
        }
        return true
    }
}
