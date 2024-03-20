package com.example.mobproj

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.mobproj.Repositories.AlbumRepository
import com.example.mobproj.Repositories.UserRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import org.checkerframework.checker.nullness.qual.NonNull
import java.util.Calendar

class ProfileActivity : AppCompatActivity() {
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var birthdate: EditText

    override fun onResume(){
        super.onResume()
        setContentView(R.layout.activity_profile)
        var sp = getSharedPre0ferences("PC", Context.MODE_PRIVATE);
        sp.edit().putString("TY","9").commit();
        var image:ImageView = findViewById(R.id.profile_imageView)
        var firstname: EditText = findViewById(R.id.firstname_edit)
        var secondname: EditText = findViewById(R.id.secondname_edit)
        var favartist:EditText = findViewById(R.id.favartist_edit)
        var favalbum:EditText = findViewById(R.id.favalbum_edit)
        var sex:Switch = findViewById(R.id.sex_switch)
        var savebutton: Button = findViewById(R.id.save_button)
        var logoutbutton: Button = findViewById(R.id.logout_button)
        var about:EditText = findViewById(R.id.about_edit)
        val address:EditText = findViewById(R.id.address_editText)
        val number: TextView = findViewById(R.id.numb_textView)
        birthdate = findViewById(R.id.birthdate_edit)
        var likes:ArrayList<String> = arrayListOf()
        val auth = UserRepository.getAuth()
        val user = auth.currentUser?.uid


        if (user != null) {
            AlbumRepository.getFirestore().collection("users").document(user)
                .get()
                .addOnSuccessListener { result ->
                    var userdata = result.data
                    firstname.setText(userdata?.get("firstname") as? String)
                    secondname.setText(userdata?.get("secondname") as? String)
                    favalbum.setText(userdata?.get("favalbum") as? String)
                    favartist.setText(userdata?.get("favartist") as? String)
                    sex.isChecked = (userdata)?.get("sex") as Boolean
                    about.setText(userdata["about"] as? String)
                    address.setText(userdata["address"] as? String)
                    likes = userdata["inFavourites"] as ArrayList<String>
                    number.setText(likes.count().toString())
                    val imagestr = userdata["image"] as? String
                    if (imagestr != null) {
                        AlbumRepository.getUserImage(
                            imagePath = imagestr,
                            onSuccess = {file ->
                                val imageUri = Uri.fromFile(file)
                                image.setImageURI(imageUri)

                            },
                            onFailure = { errorMessage ->
                                // Обработка ошибки, если не удалось получить изображение альбома
                                println("Failed to fetch album image: $errorMessage")
                            }
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
        logoutbutton.setOnClickListener(){
            auth.signOut()
            sp.edit().putString("TY","-9").commit();
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }

        savebutton.setOnClickListener(){
            var userentity = hashMapOf(
                "image" to "avatar/Без имени-3.png",
                "about" to about.text.toString(),
                "firstname" to firstname.text.toString(),
                "secondname" to secondname.text.toString(),
                "birthdate" to birthdate.text.toString(),
                "albums_in_fav" to 0,
                "sex" to sex.isChecked,
                "favalbum" to favalbum.text.toString(),
                "favartist" to favartist.text.toString(),
                "address" to address.text.toString(),
                "inFavourites" to likes
            )
            val userDocumentRef =
                UserRepository.getAuth().currentUser?.uid?.let { it1 ->
                    AlbumRepository.getFirestore().collection("users").document(
                        it1
                    )
                }

            // Добавляем запись в базу данных
            userDocumentRef?.set(userentity)
                ?.addOnSuccessListener {
                    // Запись успешно добавлена
                    Log.d("ProfileActivity", "User profile successfully created!")
                }
                ?.addOnFailureListener { e ->
                    // В случае ошибки при добавлении записи
                    Log.e("ProfileActivity", "Error adding user profile", e)
                }
        }

        birthdate.setOnClickListener(){
            showDatePickerDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        var sp = getSharedPreferences("PC", Context.MODE_PRIVATE);
        sp.edit().putString("TY","9").commit();
        var image:ImageView = findViewById(R.id.profile_imageView)
        var firstname: EditText = findViewById(R.id.firstname_edit)
        var secondname: EditText = findViewById(R.id.secondname_edit)
        var favartist:EditText = findViewById(R.id.favartist_edit)
        var favalbum:EditText = findViewById(R.id.favalbum_edit)
        var sex:Switch = findViewById(R.id.sex_switch)
        var savebutton: Button = findViewById(R.id.save_button)
        var logoutbutton: Button = findViewById(R.id.logout_button)
        var about:EditText = findViewById(R.id.about_edit)
        val address:EditText = findViewById(R.id.address_editText)
        val number: TextView = findViewById(R.id.numb_textView)
        birthdate = findViewById(R.id.birthdate_edit)
        var likes:ArrayList<String> = arrayListOf()
        val auth = UserRepository.getAuth()
        val user = auth.currentUser?.uid
        if (user != null) {
            AlbumRepository.getFirestore().collection("users").document(user)
                .get()
                .addOnSuccessListener { result ->
                    var userdata = result.data
                    firstname.setText(userdata?.get("firstname") as? String)
                    secondname.setText(userdata?.get("secondname") as? String)
                    favalbum.setText(userdata?.get("favalbum") as? String)
                    favartist.setText(userdata?.get("favartist") as? String)
                    sex.isChecked = (userdata)?.get("sex") as Boolean
                    about.setText(userdata["about"] as? String)
                    address.setText(userdata["address"] as? String)
                    likes = userdata["inFavourites"] as ArrayList<String>
                    number.setText(likes.count().toString())
                    birthdate.setText(userdata["birthdate"] as String)
                    val imagestr = userdata["image"] as? String
                    if (imagestr != null) {
                        AlbumRepository.getUserImage(
                            imagePath = imagestr,
                            onSuccess = {file ->
                                val imageUri = Uri.fromFile(file)
                                image.setImageURI(imageUri)

                            },
                            onFailure = { errorMessage ->
                                // Обработка ошибки, если не удалось получить изображение альбома
                                println("Failed to fetch album image: $errorMessage")
                            }
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
        logoutbutton.setOnClickListener(){
            auth.signOut()
            sp.edit().putString("TY","-9").commit();
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }

        savebutton.setOnClickListener(){
            var userentity = hashMapOf(
                "image" to "avatar/Без имени-3.png",
                "about" to about.text.toString(),
                "firstname" to firstname.text.toString(),
                "secondname" to secondname.text.toString(),
                "birthdate" to birthdate.text.toString(),
                "albums_in_fav" to 0,
                "sex" to sex.isChecked,
                "favalbum" to favalbum.text.toString(),
                "favartist" to favartist.text.toString(),
                "address" to address.text.toString(),
                "inFavourite" to likes
            )
            val userDocumentRef =
                UserRepository.getAuth().currentUser?.uid?.let { it1 ->
                    AlbumRepository.getFirestore().collection("users").document(
                        it1
                    )
                }

            // Добавляем запись в базу данных
            userDocumentRef?.set(userentity)
                ?.addOnSuccessListener {
                    // Запись успешно добавлена
                    Log.d("ProfileActivity", "User profile successfully created!")
                }
                ?.addOnFailureListener { e ->
                    // В случае ошибки при добавлении записи
                    Log.e("ProfileActivity", "Error adding user profile", e)
                }
        }

        birthdate.setOnClickListener(){
            showDatePickerDialog()
        }

    }
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateString = "${dayOfMonth}.${month + 1}.${year}" // Форматируйте дату по вашему усмотрению
                birthdate.setText(dateString)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Устанавливаем максимальную дату (текущую дату)
        datePickerDialog.show()
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