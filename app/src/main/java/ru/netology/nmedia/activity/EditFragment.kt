package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentEditBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class EditFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditBinding.inflate(inflater, container, false)
        val idPost = arguments?.getLong("idPost")
        if (idPost != null) {
            binding.edit.requestFocus()
            binding.edit.setText(arguments?.getString("content"))
            binding.ok.setOnClickListener {
                if (!binding.edit.text.isNullOrBlank()) {
                    viewModel.data.value?.posts?.forEach {
                        if (it.id == idPost) {
                            viewModel.edit(it)
                            viewModel.changeContent(binding.edit.text.toString())
                        }
                    }
                    AndroidUtils.hideKeyboard(binding.root)
                }
                findNavController().navigateUp()
            }
        }
        return binding.root
    }
}