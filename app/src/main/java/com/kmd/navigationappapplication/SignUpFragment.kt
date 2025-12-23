package com.kmd.navigationappapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kmd.navigationappapplication.databinding.FragmentSignUpBinding
import org.json.JSONObject

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)

        binding.txtGotoLogin.text =
            HtmlCompat.fromHtml("<u>Login</u>", HtmlCompat.FROM_HTML_MODE_LEGACY)

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
            SignUpAction()
        }

        return binding.root
    }

    private fun SignUpAction() {
        val firstName = binding.editFirstName.text.toString()
        val lastName = binding.editLastName.text.toString()
        val email = binding.editEmail.text.toString()
        val username = binding.editUsername.text.toString()
        val password = binding.editPassword.text.toString()


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

//        Toast.makeText(context, "Register successful", Toast.LENGTH_SHORT).show()
//        val action = SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()
//        findNavController().navigate(action)

        registerUser(firstName, lastName, username, email, password)
    }

    private fun registerUser(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        password: String
    ) {
        val url = "http://192.168.100.29:8000/registerUser.php"
        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                val obj = JSONObject(response)
                if (obj.get("statusCode") == 200) {
                    Toast.makeText(context, obj.get("message").toString(), Toast.LENGTH_SHORT).show()
                    val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
                    findNavController().navigate(action)
                } else if (obj.get("statusCode") == 409) {
                    Log.d("Sign Up", "***Response $obj")
                    showAlert(obj.get("message").toString())
                }
            } catch (e: Exception) {
                Log.d("Sign Up", "***Error: ${e.message}")
                showAlert("Something went wrong.")
            }
        }, Response.ErrorListener { error ->
            Log.d("Sign Up", "***Error: $error")
        }) {
            override fun getParams(): Map<String?, String?> {
                return mapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "username" to username,
                    "email" to email,
                    "password" to password,
                    "remark" to "User"
                )
            }
        }

        Volley.newRequestQueue(context).add(request)
        Log.d("Sign Up Request", "***Register User***")
    }

    private fun showAlert(message: String) {
        val alert = AlertDialog.Builder(requireContext())
        alert.setTitle("Sorry")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
        val alertDialog = alert.create()
        alertDialog.show()
    }
}
