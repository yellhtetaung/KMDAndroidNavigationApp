package com.kmd.navigationappapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kmd.navigationappapplication.databinding.FragmentWelcomeUserBinding

class WelcomeUserFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeUserBinding.inflate(inflater, container, false)

        val id = arguments?.getInt("id")
        val firstName = arguments?.getString("firstName")
        val lastName = arguments?.getString("lastName")
        val username = arguments?.getString("username")

        Log.d("Welcome user fragment", "****** $id $firstName $lastName $username")

        binding.txtWelcome.text =
            "Welcome\n Id: $id\n FirstName: $firstName\n LastName: $lastName\n Username: $username"

        return binding.root
    }
}
