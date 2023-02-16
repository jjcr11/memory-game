package com.jjcr11.memorygame.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjcr11.memorygame.databinding.FragmentBackdropBinding

class BackdropFragment : Fragment() {

    private lateinit var binding: FragmentBackdropBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackdropBinding.inflate(inflater, container, false)
        return binding.root
    }
}