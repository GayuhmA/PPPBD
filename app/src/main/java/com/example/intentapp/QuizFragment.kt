package com.example.intentapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.viewpager2.widget.ViewPager2

class QuizFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        val btnBackHome = view.findViewById<AppCompatButton>(R.id.btn_back_home)
        btnBackHome.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)
            viewPager.currentItem = 0
        }

        return view
    }
}
