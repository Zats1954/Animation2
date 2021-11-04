package ru.netology.nmedia.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryNetImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "Аноним",
    authorAvatar = "netology.png",
    likedByMe = false,
    published = System.currentTimeMillis(),
    likes = 0,
    shares = 0,
    visibles = 0,
    videoVisibility = View.INVISIBLE,
    video = " "
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryNetImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    val edited = MutableLiveData(empty)

    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            _data.postValue(FeedModel(loading = true))
            val value = try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedModel(error = true)
            }
            _data.postValue(value)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        edited.value?.let { editPost ->
            if (editPost.content == text) return
            thread {
                _data.postValue(
                    _data.value?.copy(
                        posts = _data.value?.posts.orEmpty().map { post ->
                            if (post.id == editPost.id) {
                                repository.save(editPost.copy(content = text))
                            } else {
                                post
                            }
                        })
                )

            }
        }
    }

    fun likeById(id: Long) {
        thread {
            _data.postValue(
                _data.value?.copy(
                    posts = _data.value?.posts.orEmpty().map { post ->
                        if (post.id == id) {
                            if (post.likedByMe == false)
                                repository.likeById(id)
                            else
                                repository.unlikeById(id)
                        } else {
                            post
                        }
                    })
            )
        }
    }

    fun removeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(
                    posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id })
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(old))
            }
        }
    }

    fun video(post: Post) {
        edited.value?.let {
            repository.addVideo(post)
            edited.value = post
        }
    }

    fun addPost(content: String) {
        val newPost = empty
        newPost.content = content
        newPost.let {
            thread {
//                it.published =
//                    SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault()).format(Date())
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.value = newPost
    }
}