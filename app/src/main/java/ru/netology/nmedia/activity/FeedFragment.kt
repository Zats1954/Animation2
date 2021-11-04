package ru.netology.nmedia.activity


import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.VideoFragment.Companion.idArgument
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =FragmentFeedBinding.inflate(inflater, container, false)
        val adapter = PostAdapter(
            onInteractionListener = object : OnInteractionListener {
                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onEdit(post: Post) {
                    findNavController().navigate(R.id.action_feedFragment_to_editFragment,
                    bundleOf("content" to  post.content, "idPost" to post.id))
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    if (intent.resolveActivity(requireContext().packageManager) != null)
                        startActivity(shareIntent)
                    else {
                        showToast(R.string.app_not_found_error)
                    }
                }

                override fun onVideo(post: Post) {
                    findNavController().navigate(R.id.action_feedFragment_to_videoFragment,
                        Bundle().apply { idArgument = post }
                    )
                }

                override fun playVideo(post: Post) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(post.video?.trim())
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    if (intent.resolveActivity(requireContext().packageManager) != null)
                        startActivity(shareIntent)
                    else {
                        showToast(R.string.app_not_found_error)
                    }
                }
            })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.posts)
            binding.progress.isVisible = state.loading
            binding.errorGroup.isVisible = state.error
            binding.emptyText.isVisible = state.empty
        })

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newFragment,
            bundleOf("content" to " " ))
        }
        return binding.root
    }


    fun showToast(text: Int, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(
            context,
            getString(text),
            length
        ).show()
    }
}


