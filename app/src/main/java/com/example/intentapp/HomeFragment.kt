package com.example.intentapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.viewpager2.widget.ViewPager2

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnMateri = view.findViewById<AppCompatButton>(R.id.btn_materi)
        val btnQuiz = view.findViewById<AppCompatButton>(R.id.btn_quiz)

        btnMateri.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)
            viewPager.currentItem = 1
        }

        btnQuiz.setOnClickListener {
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)
            viewPager.currentItem = 2
        }

        return view
    }
}
