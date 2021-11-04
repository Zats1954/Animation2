package ru.netology.nmedia.repository


import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Post1

interface PostRepository {
    fun likeById(id:Long):Post
    fun unlikeById(id:Long):Post
    fun getAll(): List<Post>
    fun removeById(id: Long)
    fun save(post: Post):Post
    fun addVideo(post: Post)
    fun getById(id: Long): Post
}