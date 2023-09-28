package com.example.educonnect.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.educonnect.util.ThemeManager
import com.example.educonnect.data.Article
import com.example.educonnect.data.FeaturedImage
import com.example.educonnect.data.User
import com.example.educonnect.data.UserChats
import com.example.educonnect.data.Video
import com.example.educonnect.util.Constants.Companion.THEME_DARK
import com.example.educonnect.util.Constants.Companion.THEME_LIGHT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    lateinit var databaseReference: DatabaseReference
    private val firestore = FirebaseFirestore.getInstance()

    private var userChatLiveData = MutableLiveData<List<UserChats>>()
    private var userDataLiveData = MutableLiveData<User>()

    private val featureImageLiveData = MutableLiveData<FeaturedImage>()
    private var articlesLiveData = MutableLiveData<List<Article>>()
    private var videosLiveData = MutableLiveData<List<Video>>()

    private val userId = auth.currentUser?.uid

    private val themePreferenceLiveData = MutableLiveData<String>()
    private val themeChangedLiveData = MutableLiveData<Boolean>()

    private val _errorLiveData = MutableLiveData<String>()
    val observeError: LiveData<String> get() = _errorLiveData

    var isThemeChangePending = false

    val themeChanged: LiveData<Boolean>
        get() = themeChangedLiveData

    init {
        val savedTheme = getThemePreference()
        themePreferenceLiveData.value = savedTheme
        ThemeManager.applyTheme(savedTheme)
    }

    // Chat Fragment
    fun getUserChat() {
        databaseReference =
            userId?.let { database.getReference("users").child(it).child("chats") }!!

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val userList = mutableListOf<UserChats>()
                for (dataSnapshot in snapshot.children) {

                    val user = dataSnapshot.getValue(UserChats::class.java)
                    user?.let {
                        userList.add(user)
                    }
                }

                userChatLiveData.value = userList
            }

            override fun onCancelled(error: DatabaseError) {
                _errorLiveData.value = error.toString()
            }

        })
    }

    fun observeUserChatsLiveData(): LiveData<List<UserChats>> {
        return userChatLiveData
    }

    // Profile Fragment
    fun getUserData() {
        if (userId != null) {
            database.reference.child("users").child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val userData = snapshot.getValue(User::class.java)!!
                        userDataLiveData.value = userData
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _errorLiveData.value = error.toString()
                    }

                })
        }

    }

    fun observeUserData(): LiveData<User> {
        return userDataLiveData
    }


    // Home Fragment
    fun getArticles() {
        firestore.collection("articles")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val articleList = mutableListOf<Article>()
                for (document in querySnapshot) {
                    val title = document.getString("title") ?: ""
                    val author = document.getString("author") ?: ""
                    val publishTime = document.getString("publishTime") ?: ""
                    val description = document.getString("description") ?: ""
                    val imageLink = document.getString("imageLink") ?: ""
                    val articles = Article(title, description, publishTime, author, imageLink)
                    articleList.add(articles)
                }
                articlesLiveData.value = articleList
            }
            .addOnFailureListener { exception ->
                // Handle the failure to retrieve data
                _errorLiveData.value = exception.toString()
            }
    }

    fun observeArticlesLiveData(): LiveData<List<Article>> {
        return articlesLiveData
    }

    // Home Fragment
    fun getVideos() {
        firestore.collection("videos")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val videoList = mutableListOf<Video>()
                for (document in querySnapshot) {
                    val title = document.getString("title") ?: ""
                    val author = document.getString("author") ?: ""
                    val publishTime = document.getString("publishTime") ?: ""
                    val description = document.getString("description") ?: ""
                    val imageLink = document.getString("imageLink") ?: ""
                    val videoLink = document.getString("videoLink")?: ""
                    val video = Video(title, description, publishTime, author, imageLink, videoLink)
                    videoList.add(video)
                }
                videosLiveData.value = videoList
            }
            .addOnFailureListener { exception ->
                // Handle the failure to retrieve data
                _errorLiveData.value = exception.toString()
            }
    }

    fun observeVideosLiveData(): LiveData<List<Video>> {
        return videosLiveData
    }

    // Home Fragment
    fun getFeaturedImage() {
        database.reference.child("featuredImages")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val featuredImage = snapshot.getValue(FeaturedImage::class.java)!!
                    featureImageLiveData.value = featuredImage
                }

                override fun onCancelled(error: DatabaseError) {
                    _errorLiveData.value = error.toString()
                }

            })
    }

    fun observeFeaturedImageLiveData(): LiveData<FeaturedImage> {
        return featureImageLiveData
    }

    // Profile Fragment
    fun logout() {
        if (userId != null) {
            database.reference.child("users").child(userId).child("token").setValue("null")
        }
        FirebaseAuth.getInstance().signOut()
    }

    // Profile Fragment
    fun toggleTheme() {
        val currentTheme = themePreferenceLiveData.value
        val newTheme = when (currentTheme) {
            THEME_LIGHT -> THEME_DARK
            THEME_DARK -> THEME_LIGHT
            else -> THEME_LIGHT
        }
        themePreferenceLiveData.value = newTheme
        isThemeChangePending = true
        ThemeManager.saveThemePreference(getApplication(), newTheme)
        ThemeManager.applyTheme(newTheme)
    }

    private fun getThemePreference(): String {
        return ThemeManager.getThemePreference(getApplication())
    }

}