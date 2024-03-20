package com.example.mobproj

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.mobproj.Repositories.AlbumRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        var sp = getSharedPreferences("PC", Context.MODE_PRIVATE).edit();

            var auth = FirebaseAuth.getInstance();
            val db = FirebaseFirestore.getInstance();
            var userLogin: EditText = findViewById(R.id.user_login);
            var userPassword: EditText = findViewById(R.id.user_password);
            var ok_button: Button = findViewById(R.id.signup_button);
            var login:TextView = findViewById(R.id.signin_textView)
            login.setOnClickListener(){
                val intent = Intent(this,SignInActivity::class.java)
                startActivity(intent);
                finish()
            }
            ok_button.setOnClickListener(){
                var login = userLogin.text.toString().trim();
                var password = userPassword.text.toString().trim();

                if (login == " " || password == " " || login.isEmpty() || password.isEmpty()){
                    Toast.makeText(this,"Заполните поля", Toast.LENGTH_LONG).show();
                }else{
                    var user = User(login,password);
                    auth.createUserWithEmailAndPassword(login,password).addOnCompleteListener{ task ->
                        if (task.isSuccessful){
                            val user = auth.currentUser;
                            val arr:ArrayList<String> = arrayListOf()
                            val userentity = hashMapOf(
                                "image" to "avatar/Без имени-3.png",
                                "about" to "idk",
                                "firstname" to "",
                                "secondname" to "",
                                "birthdate" to Timestamp.now(),
                                "albums_in_fav" to 0,
                                "favalbum" to "",
                                "favartist" to "",
                                "sex" to true,
                                "address" to "No Address",
                                "inFavourites" to arr
                            )
                            val userDocumentRef =
                                user?.uid?.let { it1 ->
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
                            var intent = Intent(this,ProfileActivity::class.java);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(this,"Произошла ошибка", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }





    }
}