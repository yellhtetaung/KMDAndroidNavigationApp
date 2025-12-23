package com.kmd.navigationappapplication

import android.content.Intent
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
import com.kmd.navigationappapplication.databinding.FragmentLoginBinding
import org.json.JSONObject

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.txtGotoSignUp.text = HtmlCompat.fromHtml("<u>Sign Up</u>", HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.txtGotoSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }

        binding.btnLogInClear.setOnClickListener {
            binding.editLogInUsername.setText("")
            binding.editLogInPassword.setText("")
        }

        binding.btnLogInLogin.setOnClickListener {
            onLoginHandler()
        }

        return binding.root
    }

    private fun onLoginHandler() {
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

//        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
//        findNavController().navigate(action)
        loginUser(username, password)
    }

    private fun loginUser(
        username: String,
        password: String
    ) {
        val url = "http://192.168.100.29:8000/loginUser.php"
        val request = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                try {
                    val obj = JSONObject(response)
                    Log.d("Login", "***Response: $obj")
                    if (obj.get("statusCode") == 200) {
                        val user = obj.getJSONObject("data")
                        Log.d("Login", "***Response: $user")
                        val str =
                            "Hello, ${user.getString("firstName")} ${user.getString("lastName")}"

                        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()

//                    val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
//                    findNavController().navigate(action)

                        // Load the user activity
                        val intent = Intent(context, UserActivity::class.java)

                        intent.putExtra("id", user.getInt("id"))
                        intent.putExtra("firstName", user.getString("firstName"))
                        intent.putExtra("lastName", user.getString("lastName"))
                        intent.putExtra("username", user.getString("username"))

                        startActivity(intent)
                        activity?.finish()
                    } else {
                        Log.d("Login", "***Response $obj")
                        showAlert(obj.get("message").toString())
                    }
                }
                catch (e: Exception) {
                    Log.d("Login", "***Error: ${e.message}")
                    showAlert("Something went wrong.")
                }
            },
            Response.ErrorListener { error ->
                Log.d("Login", "***Error: $error")
            }) {
            override fun getParams(): Map<String?, String?> {
                return mapOf(
                    "username" to username,
                    "password" to password,
                )
            }
        }
        Volley.newRequestQueue(context).add(request)
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
