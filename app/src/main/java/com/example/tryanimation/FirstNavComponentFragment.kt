package com.example.tryanimation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation


class FirstNavComponentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first_nav_component, container, false)

        val tvPage1 = view.findViewById<TextView>(R.id.tvPage1)
        tvPage1.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_firstNavComponentFragment_to_secondNavComponentFragment) }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}