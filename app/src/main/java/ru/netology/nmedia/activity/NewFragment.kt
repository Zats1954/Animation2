package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewBinding.inflate(inflater, container, false)
        binding.edit.requestFocus()
        binding.edit.setText(arguments?.getString("content"))
        binding.ok.setOnClickListener {
            if (!binding.edit.text.isNullOrBlank()) {
                viewModel.addPost(binding.edit.text.toString())
                AndroidUtils.hideKeyboard(binding.root)
            }
            viewModel.postCreated.observe(viewLifecycleOwner){
            viewModel.loadPosts()
            findNavController().navigateUp()}
        }
        return binding.root
    }
}