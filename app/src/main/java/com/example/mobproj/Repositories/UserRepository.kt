package com.example.mobproj.Repositories

import com.example.mobproj.Album
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object UserRepository {
    private var initialized = false
    private lateinit var auth : FirebaseAuth


    fun initialize() {
        if (!initialized) {
            auth = FirebaseAuth.getInstance()
            initialized = true
        }
    }

    fun getAuth(): FirebaseAuth {
        if (!initialized) {
            throw IllegalStateException("Repository has not been initialized")
        }
        return auth
    }

}