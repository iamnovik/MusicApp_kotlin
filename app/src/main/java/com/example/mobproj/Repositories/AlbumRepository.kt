package com.example.mobproj.Repositories

import com.example.mobproj.Album
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

object AlbumRepository {

        private var initialized = false
        private lateinit var firestore: FirebaseFirestore
        private lateinit var storage: FirebaseStorage

        fun initialize() {
            if (!initialized) {
                firestore = FirebaseFirestore.getInstance()
                storage = FirebaseStorage.getInstance()
                initialized = true
            }
        }

        fun getFirestore(): FirebaseFirestore {
            if (!initialized) {
                throw IllegalStateException("Repository has not been initialized")
            }
            return firestore
        }

        fun getStorage(): FirebaseStorage {
            if (!initialized) {
                throw IllegalStateException("Repository has not been initialized")
            }
            return storage
        }

    fun getAllAlbums(onSuccess: (List<Album>) -> Unit, onFailure: (String) -> Unit) {
        firestore.collection("albums")
            .get()
            .addOnSuccessListener { result ->
                val albums = mutableListOf<Album>()
                for (document in result) {
                    val album = document.toObject(Album::class.java)
                    albums.add(album)
                }
                onSuccess(albums)
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Failed to fetch albums")
            }
    }
    fun getUserImage(imagePath: String?, onSuccess: (File) -> Unit, onFailure: (String) -> Unit) {
        imagePath?.let { path ->
            val storageRef = storage.reference.child(path)
            val localFile = File.createTempFile("tempImage", "jpg")

            storageRef.getFile(localFile)
                .addOnSuccessListener {
                    onSuccess(localFile)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Failed to fetch image")
                }
        } ?: run {
            onFailure("Image path is null")
        }
    }
    fun getAlbumImage(imagePath: String?, onSuccess: (File) -> Unit, onFailure: (String) -> Unit) {
        imagePath?.let { path ->
            val storageRef = storage.reference.child(path)
            val localFile = File.createTempFile("tempImage", "jpg")

            storageRef.getFile(localFile)
                .addOnSuccessListener {
                    onSuccess(localFile)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Failed to fetch image")
                }
        } ?: run {
            onFailure("Image path is null")
        }
    }
}