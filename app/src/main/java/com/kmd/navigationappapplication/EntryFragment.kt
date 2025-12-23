package com.kmd.navigationappapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kmd.navigationappapplication.databinding.FragmentEntryBinding
import org.json.JSONObject

class EntryFragment : Fragment() {
    private lateinit var binding: FragmentEntryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryBinding.inflate(inflater, container, false)

        val userId = arguments?.getInt("id")
        val firstName = arguments?.getString("firstName")
        val lastName = arguments?.getString("lastName")
        val username = arguments?.getString("username")

        Log.d("Entry fragment", "****** $userId $firstName $lastName $username")

        val activityObj = arguments?.getSerializable(
            "fitnessActivity",
            FitnessActivity::class.java
        )

        if (activityObj != null) {
            binding.apply {
                editActivityType.setText(activityObj.type)
                editEntryDate.setText(activityObj.entryDate)
                editDay.setText(activityObj.day)
                editMonth.setText(activityObj.month)
                editYear.setText(activityObj.year)
                editTime.setText(activityObj.time)
                editDuration.setText(activityObj.duration)
                editDistance.setText(activityObj.distance.toString())
                editWeight.setText(activityObj.weight.toString())
                editPlace.setText(activityObj.place)
                editRemark.setText(activityObj.remark)

                btnActivitySave.text = "Edit"
            }
        }

        binding.btnActivityCancel.setOnClickListener {
            activityClear()
        }

        binding.btnActivitySave.setOnClickListener {
            val type = binding.editActivityType.text.toString()
            val entryDate = binding.editEntryDate.text.toString()
            val day = binding.editDay.text.toString()
            val month = binding.editMonth.text.toString()
            val year = binding.editYear.text.toString()
            val time = binding.editTime.text.toString()
            val duration = binding.editDuration.text.toString()
            val distance = binding.editDistance.text.toString()
            val weight = binding.editWeight.text.toString()
            val place = binding.editPlace.text.toString()
            val remark = binding.editRemark.text.toString()

            if (type.isEmpty()) {
                binding.editActivityType.error = "Please enter activity type."
            } else if (entryDate.isEmpty()) {
                binding.editEntryDate.error = "Please enter entry date."
            } else if (day.isEmpty()) {
                binding.editDay.error = "Please enter day."
            } else if (month.isEmpty()) {
                binding.editMonth.error = "Please enter month."
            } else if (year.isEmpty()) {
                binding.editYear.error = "Please enter year."
            } else if (time.isEmpty()) {
                binding.editTime.error = "Please enter time."
            } else if (duration.isEmpty()) {
                binding.editDuration.error = "Please enter duration."
            } else if (distance.isEmpty()) {
                binding.editDistance.error = "Please enter distance."
            } else if (weight.isEmpty()) {
                binding.editWeight.error = "Please enter weight."
            } else if (place.isEmpty()) {
                binding.editPlace.error = "Please enter place."
            } else if (remark.isEmpty()) {
                binding.editRemark.error = "Please enter remark."
            } else {
                if (activityObj == null) {
                    val fitnessActivity = FitnessActivity(
                        id = 0,
                        type = type,
                        entryDate = entryDate,
                        day = day.toInt(),
                        month = month.toInt(),
                        year = year.toInt(),
                        time = time,
                        duration = duration.toInt(),
                        distance = distance.toDouble(),
                        weight = weight.toDouble(),
                        place = place,
                        remark = remark,
                        userId = userId!!
                    )

                    Log.d("Entry fragment", "****** $fitnessActivity")
                    saveActivity(fitnessActivity)
                    activityClear()
                } else {
                    val fitnessActivity = FitnessActivity(
                        id = activityObj.id,
                        type = type,
                        entryDate = entryDate,
                        day = day.toInt(),
                        month = month.toInt(),
                        year = year.toInt(),
                        time = time,
                        duration = duration.toInt(),
                        distance = distance.toDouble(),
                        weight = weight.toDouble(),
                        place = place,
                        remark = remark,
                        userId = userId!!
                    )

                    Log.d("Entry fragment", "****** $fitnessActivity")
                    editActivity(fitnessActivity)
                    activityClear()
                }
            }
        }

        return binding.root
    }

    private fun editActivity(fitnessActivity: FitnessActivity) {
        val url = "http://192.168.100.29:8000/updateActivity.php"
        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                Log.d("Entry Fragment", "***** Response: $response")
                val obj = JSONObject(response)
                Log.d("Entry Fragment", "***** Json obj: $obj")
                if (obj.get("statusCode") == 200) Toast.makeText(
                    context,
                    obj.get("message").toString(),
                    Toast.LENGTH_SHORT
                ).show()
                else if (obj.get("statusCode") == 409) {
                    Log.d("Sign Up", "***Response $obj")
                    showAlert(obj.get("message").toString())
                }
            } catch (e: Exception) {
                Log.d("Entry Fragment", "***** Error: ${e.message}")
            }
        }, Response.ErrorListener { error ->
            Log.d("Entry Fragment", "***** Error: $error")
        }) {
            override fun getParams(): Map<String?, String?> {
                return mapOf(
                    "type" to fitnessActivity.type,
                    "entryDate" to fitnessActivity.entryDate,
                    "day" to fitnessActivity.day.toString(),
                    "month" to fitnessActivity.month.toString(),
                    "year" to fitnessActivity.year.toString(),
                    "time" to fitnessActivity.time,
                    "duration" to fitnessActivity.duration.toString(),
                    "distance" to fitnessActivity.distance.toString(),
                    "weight" to fitnessActivity.weight.toString(),
                    "place" to fitnessActivity.place,
                    "remark" to fitnessActivity.remark,
                    "userId" to fitnessActivity.userId.toString()
                )
            }
        }

        Volley.newRequestQueue(context).add(request)
        Log.d("Entry Request", "***Edit Activity***")
    }

    private fun activityClear() {
        binding.apply {
            editActivityType.text.clear()
            editEntryDate.text.clear()
            editDay.text.clear()
            editMonth.text.clear()
            editYear.text.clear()
            editTime.text.clear()
            editDuration.text.clear()
            editDistance.text.clear()
            editWeight.text.clear()
            editPlace.text.clear()
            editRemark.text.clear()
        }
    }

    private fun saveActivity(fitnessActivity: FitnessActivity) {
        val url = "http://192.168.100.29:8000/insertActivity.php"
        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                Log.d("Entry Fragment", "***** Response: $response")
                val obj = JSONObject(response)
                Log.d("Entry Fragment", "***** Json obj: $obj")
                if (obj.get("statusCode") == 200) Toast.makeText(
                    context,
                    obj.get("message").toString(),
                    Toast.LENGTH_SHORT
                ).show()
                else if (obj.get("statusCode") == 409) {
                    Log.d("Sign Up", "***Response $obj")
                    showAlert(obj.get("message").toString())
                }
            } catch (e: Exception) {
                Log.d("Entry Fragment", "***** Error: ${e.message}")
            }
        }, Response.ErrorListener { error ->
            Log.d("Entry Fragment", "***** Error: $error")
        }) {
            override fun getParams(): Map<String?, String?> {
                return mapOf(
                    "type" to fitnessActivity.type,
                    "entryDate" to fitnessActivity.entryDate,
                    "day" to fitnessActivity.day.toString(),
                    "month" to fitnessActivity.month.toString(),
                    "year" to fitnessActivity.year.toString(),
                    "time" to fitnessActivity.time,
                    "duration" to fitnessActivity.duration.toString(),
                    "distance" to fitnessActivity.distance.toString(),
                    "weight" to fitnessActivity.weight.toString(),
                    "place" to fitnessActivity.place,
                    "remark" to fitnessActivity.remark,
                    "userId" to fitnessActivity.userId.toString()
                )
            }
        }

        Volley.newRequestQueue(context).add(request)
        Log.d("Entry Request", "***Insert Activity***")
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
