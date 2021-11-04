package ru.netology.nmedia.dto


import android.os.Parcelable
import android.view.View
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    var published: Long,
    var content: String,
    val likedByMe: Boolean,
    val likes: Int,
    val shares: Int,
    val visibles: Int,
    val videoVisibility: Int = View.INVISIBLE,
    var video: String? = " "
) : Parcelable