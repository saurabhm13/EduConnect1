package com.example.educonnect.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.educonnect.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class EditProfileViewModel() : ViewModel(){

    private val auth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference

    private val userId = auth.currentUser?.uid
    var errorCallback: (() -> Unit)? = null

    fun saveUserData(name: String, image: Uri, email: String) {

        val imageUri: Uri = image
        val imageRef = storageReference.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully
            // Now, get the download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // The 'uri' variable now contains the download URL of the uploaded image
                val imageUrl = uri.toString()

                if (userId != null) {
                    databaseReference.child("users").child(userId).child("name").setValue(name)
                    databaseReference.child("users").child(userId).child("email").setValue(email)
                    databaseReference.child("users").child(userId).child("image").setValue(imageUrl)
                }
            }.addOnFailureListener { exception ->
                // Handle the failure to get the download URL
                errorCallback?.invoke()
            }
        }.addOnFailureListener { exception ->
            // Handle the failure to upload the image
            errorCallback?.invoke()
        }
    }

    fun saveUserData(name: String, email: String) {
        if (userId != null) {
            databaseReference.child("users").child(userId).child("name").setValue(name)
            databaseReference.child("users").child(userId).child("email").setValue(email)
        }
    }

}