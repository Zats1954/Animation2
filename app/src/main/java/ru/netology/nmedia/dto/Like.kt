package ru.netology.nmedia.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String
): Parcelable
