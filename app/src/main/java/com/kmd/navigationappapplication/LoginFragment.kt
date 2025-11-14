package com.kmd.navigationappapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import com.kmd.navigationappapplication.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.txtGotoSignUp.setText(
            HtmlCompat.fromHtml("<u>Sign Up</u>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        )

        binding.txtGotoSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }

        binding.btnLogInClear.setOnClickListener {
            binding.editLogInUsername.setText("");
            binding.editLogInPassword.setText("");
        }

        binding.btnLogInLogin.setOnClickListener {
            loginAction()
        }

        return binding.root
    }

    private fun loginAction() {
        val username = binding.editLogInUsername.text.toString()
        val password = binding.editLogInPassword.text.toString()

        if (username.isEmpty()) {
            binding.editLogInUsername.error = "Username is required"
            return
        }

        if (password.isEmpty()) {
            binding.editLogInPassword.error = "Password is required"
            return
        }

        if (username == "admin" && password == "admin") {
            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
        }

        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        findNavController().navigate(action)
    }
}