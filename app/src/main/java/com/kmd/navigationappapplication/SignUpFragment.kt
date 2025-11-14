package com.kmd.navigationappapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import com.kmd.navigationappapplication.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater)

        binding.txtGotoLogin.text = HtmlCompat.fromHtml("<u>Login</u>", HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.txtGotoLogin.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        binding.btnClear.setOnClickListener {
            binding.apply {
                editFirstName.text.clear()
                editLastName.text.clear()
                editEmail.text.clear()
                editUsername.text.clear()
                editPassword.text.clear()
            }
        }

        binding.btnSignUp.setOnClickListener {
            SignUpAction();
        }

        return binding.root
    }

    private fun SignUpAction() {
        val firstName = binding.editFirstName.text.toString();
        val lastName = binding.editLastName.text.toString();
        val email = binding.editEmail.text.toString();
        val username = binding.editUsername.text.toString();
        val password = binding.editPassword.text.toString();


        if (firstName.isEmpty()) {
            binding.editFirstName.error = "First name is required."
            return
        }

        if (lastName.isEmpty()) {
            binding.editLastName.error = "Last name is required."
            return
        }

        if (email.isEmpty()) {
            binding.editEmail.error = "Email is required."
            return
        }

        if (username.isEmpty()) {
            binding.editUsername.error = "Username is required."
            return
        }

        if (password.isEmpty()) {
            binding.editFirstName.error = "Password is required."
            return
        }

        Toast.makeText(context, "Register successful", Toast.LENGTH_SHORT).show()
        val action = SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()
        findNavController().navigate(action)
    }
}