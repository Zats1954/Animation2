package ru.netology.nmedia.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.text.SimpleDateFormat


interface OnInteractionListener {
    fun onRemove(post: Post) {}
    fun onEdit(post: Post) {}
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onVideo(post: Post) {}
    fun playVideo(post: Post){}
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding,
            onInteractionListener)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
  if(payloads.isEmpty()){
      onBindViewHolder(holder, position)
  } else {
       payloads.forEach{
           if(it is PostPayload){
             holder.bind(it)
           }
       }
  }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(payload: PostPayload){
 /*!!!*/ var i = 0
        payload.liked?.also { liked ->
            binding.ivLike.setIconResource(
                if(liked) R.drawable.ic_heart2
                 else R.drawable.ic_heart
            )
            binding.ivLike.text = if(liked) "1" else "0"
            if(liked){
/*!!!*/                print ("i if ${++i} ")
                ObjectAnimator.ofPropertyValuesHolder(
                    binding.ivLike,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.25F,1.0F, 1.25F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.25F,1.0F, 1.25F))

            } else{
/*!!!*/                print ("i_else ${++i} ")
                ObjectAnimator.ofFloat( binding.ivLike,View.ROTATION, 0F, 360F)
            }.apply{
                repeatCount = 100

            }.start()
        }
    }

    fun bind(post: Post) {
        binding.apply {
/*!!!*/            println("post.id ${post.id}")
            tvAuthor.text = post.author
            val pattern = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            tvPublished.text = pattern.format(post.published)
            tvContent.text = post.content
            ivShare.text = intToString(post.shares)
            ivVisible.text = intToString(post.visibles)
            ivLike.isChecked = post.likedByMe
            ivLike.text = post.likes.toString()
            ivVideo.visibility = post.videoVisibility
            tvId.text = post.id.toString()

            ivMenu.setOnClickListener {
                PopupMenu(it.context, it)
                    .apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    onInteractionListener.onRemove(post)
                                    true
                                }
                                R.id.edit -> {
                                    onInteractionListener.onEdit(post)
                                    true
                                }
                                R.id.video -> {
                                    ivVideo.visibility = View.VISIBLE
                                    onInteractionListener.onVideo(post)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
            }

            ivLike.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            ivShare.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            ivVideo.setOnClickListener {
                onInteractionListener.playVideo(post)
            }
        }
    }
}

fun intToString(num: Int): String {
    val str = num.toString()
    when (str.length) {
        1, 2, 3 -> return str                     // 999
        4 -> return if (str[1] != '0') {
            str[0] + "." + str[1] + "K"    // 1100
        } else {
            str[0] + "K"        //1000
        }
        5 -> return str.substring(0, 2) + "K"            // 10000
        6 -> return str.substring(0, 3) + "K"        // 100000
        else -> return str.substring(
            0,
            str.length - 6
        ) + "." + str[str.length - 6] + "M"           //1000000
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun getChangePayload(oldItem: Post, newItem: Post): Any =
        PostPayload(
            newItem.likedByMe.takeIf { oldItem.likedByMe != it }
        )

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

data class PostPayload(
    val liked: Boolean? = null
)
