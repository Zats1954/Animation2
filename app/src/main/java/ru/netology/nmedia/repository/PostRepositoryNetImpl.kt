package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Post1
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class PostRepositoryNetImpl: PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object: TypeToken<List<Post>>(){}
    companion object {
//        private const val BASE_URL = "http://10.0.2.2:9999"
        private const val BASE_URL = "http://192.168.0.129:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun likeById(id: Long):Post {
        val request: Request =   Request.Builder()
            .post(" ".toRequestBody())
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()
         client.newCall(request).execute()
             .let{
                 it.body?.string() ?: throw RuntimeException("body is null")}
             .let{
                 return  gson.fromJson(it, Post::class.java)
             }
    }

    override fun unlikeById(id: Long):Post {
        val request: Request =   Request.Builder()
            .delete(" ".toRequestBody())
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()
        client.newCall(request).execute()
            .let{
                it.body?.string() ?: throw RuntimeException("body is null")}
            .let{
                return gson.fromJson(it, Post::class.java).copy(likes = 0)
            }
    }

    override fun getAll(): List<Post> {
        val request: Request =   Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()
        return client.newCall(request)
            .execute()
            .let {
                it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }

    override fun getById(id:Long): Post {
        val request: Request =   Request.Builder()
            .url("${BASE_URL}/api/posts/$id")
            .build()
        return client.newCall(request)
            .execute()
            .let {
                it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, Post1::class.java).PtoP()
            }
    }
    override fun removeById(id: Long) {
       val request: Request = Request.Builder()
           .delete()
           .url("${BASE_URL}/api/posts/$id")
           .build()
        client.newCall(request)
            .execute()
            .close()

    }

    override fun save(post: Post):Post  {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

       return gson.fromJson(client.newCall(request)
                            .execute()
                            .body?.string(), Post::class.java)
    }

    override fun addVideo(post: Post) {
        TODO("Not yet implemented")
    }

}