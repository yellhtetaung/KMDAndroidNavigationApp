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
    private val monthList = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

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

        binding.txtWelcome.text = "Welcome, $firstName $lastName"

        if (id != 0) {
            getTotalDuration(id!!)
        }

        return binding.root
    }

    private fun getTotalDuration(id: Int) {
        val barSet = listOf(
            "JAN" to monthList[0].toFloat(),
            "FEB" to monthList[1].toFloat(),
            "MAR" to monthList[2].toFloat(),
            "APR" to monthList[3].toFloat(),
            "MAY" to monthList[4].toFloat(),
            "JUNE" to monthList[5].toFloat(),
            "JUL" to monthList[6].toFloat(),
            "AUG" to monthList[7].toFloat(),
            "SEP" to monthList[8].toFloat(),
            "OCT" to monthList[9].toFloat(),
            "NOV" to monthList[10].toFloat(),
            "DEC" to monthList[11].toFloat()
        )
        binding.barChartReport.animate(barSet)
        binding.barChartReport.invalidate()
    }
}
