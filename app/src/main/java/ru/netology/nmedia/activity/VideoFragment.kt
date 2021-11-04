package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.PostDelegate
import ru.netology.nmedia.viewmodel.PostViewModel

class VideoFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    companion object {
        var Bundle.idArgument: Post? by PostDelegate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewBinding.inflate(inflater, container, false)
        var post: Post? = arguments?.idArgument
        if(post?.video.isNullOrBlank()) post?.video = "https://youtube.com"
        binding.edit.requestFocus()
        binding.edit.setText(post?.video)
        binding.ok.setOnClickListener {
            if (!binding.edit.text.isNullOrBlank()) {
                post =post?.copy(video = binding.edit.text.toString(), videoVisibility = View.VISIBLE)
                post?.let {viewModel.edit(it)
                           viewModel.video(it)}
                AndroidUtils.hideKeyboard(binding.root)
            }
            findNavController().navigateUp()
        }
        return binding.root
    }
}