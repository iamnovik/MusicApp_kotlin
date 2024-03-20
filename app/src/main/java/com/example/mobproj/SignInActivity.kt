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
import com.example.mobproj.Repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AlbumRepository.initialize()
        UserRepository.initialize()
        var sp = getSharedPreferences("PC", Context.MODE_PRIVATE)
        if (sp.getString("TY","-9")!="-9"){
            startActivity(Intent(this,AlbumsActivity::class.java))
        }else{
            var signUp: TextView = findViewById(R.id.signup_textView)

            signUp.setOnClickListener {
                Log.d("Tag","hello");
                val intent = Intent(this,SignUpActivity::class.java)
                Log.d("Tag",intent.toString());
                startActivity(intent);
                finish()
            }
            var userLogin: EditText = findViewById(R.id.user_login)
            var userPassword: EditText = findViewById(R.id.user_password)
            var ok_button: Button = findViewById(R.id.signin_button)

            ok_button.setOnClickListener {
                var login = userLogin.text.toString().trim()
                var password = userPassword.text.toString().trim()

                if (login == "" || password == ""){
                    Toast.makeText(this,"Заполните поля",Toast.LENGTH_LONG).show()
                }else{
                    val auth = FirebaseAuth.getInstance()
                    auth.signInWithEmailAndPassword(login, password)
                        .addOnCompleteListener(this) {  task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this,AlbumsActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this,"Проверьте данные", Toast.LENGTH_LONG).show()
                            }
                        }

                }
            }
        }

    }
}

