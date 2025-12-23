package com.kmd.navigationappapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kmd.navigationappapplication.databinding.FragmentEntryBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class EntryFragment : Fragment() {
    private lateinit var binding: FragmentEntryBinding
    private val activityType =
        listOf(
            "Choose one of the activity type...",
            "Running",
            "Swimming",
            "Cycling",
            "Weight Lifting",
            "Jogging",
            "Jumping Rope"
        )
    private var pickedEntryDate: String = ""
    private var pickedTime: String = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryBinding.inflate(inflater, container, false)

        val userId = arguments?.getInt("id")

        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, activityType)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerActivityType.adapter = spinnerAdapter

        val activityObj = arguments?.getSerializable(
            "fitnessActivity",
            FitnessActivity::class.java
        )

        Log.d("Entry fragment", "****** Activity Object: $activityObj")

        if (activityObj != null) {
            binding.apply {
                editEntryDate.text = activityObj.entryDate
                editDay.setText(activityObj.day.toString())
                editMonth.setText(activityObj.month.toString())
                editYear.setText(activityObj.year.toString())
                editTime.text = activityObj.time
                editDuration.setText(activityObj.duration.toString())
                editDistance.setText(activityObj.distance.toString())
                editWeight.setText(activityObj.weight.toString())
                editPlace.setText(activityObj.place)
                editRemark.setText(activityObj.remark)

                btnActivitySave.text = "Edit"
            }
        }

        binding.editEntryDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.editTime.setOnClickListener {
            showTimePickerDialog()
        }

        binding.btnActivityCancel.setOnClickListener {
            activityClear()
        }

        binding.btnActivitySave.setOnClickListener {
            val type = binding.spinnerActivityType.selectedItem.toString()
            val entryDate = binding.editEntryDate.text.toString()
            val day = binding.editDay.text.toString()
            val month = binding.editMonth.text.toString()
            val year = binding.editYear.text.toString()
            val time = binding.editTime.text.toString()

            var distance: Double
            var weight: Double

            val place = binding.editPlace.text.toString()
            val remark = binding.editRemark.text.toString()

            if (type == activityType[0]) {
                Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show()
            } else if (entryDate == R.string.enter_entry_date.toString()) {
                binding.editEntryDate.error = "Please pick entry date."
            } else if (day.isEmpty()) {
                binding.editDay.error = "Please enter day."
            } else if (month.isEmpty()) {
                binding.editMonth.error = "Please enter month."
            } else if (year.isEmpty()) {
                binding.editYear.error = "Please enter year."
            } else if (time == R.string.enter_time.toString()) {
                binding.editTime.error = "Please pick time."
            } else if (binding.editDuration.text.toString().isEmpty()) {
                binding.editDuration.error = "Please enter duration (minute)."
            } else if (binding.editDistance.text.toString().isEmpty()) {
                binding.editDistance.error = "Please enter distance (km)."
            } else if (binding.editWeight.text.toString().isEmpty()) {
                binding.editWeight.error = "Please enter weight(kg)."
            } else {
                val duration = binding.editDuration.text.toString().toInt()
                distance = binding.editDistance.text.toString().toDouble()
                weight = binding.editWeight.text.toString().toDouble()

                if (activityObj == null) {
                    val fitnessActivity = FitnessActivity(
                        id = 0,
                        type = type,
                        entryDate = entryDate,
                        day = day.toInt(),
                        month = month.toInt(),
                        year = year.toInt(),
                        time = time,
                        duration = duration,
                        distance = distance,
                        weight = weight,
                        place = place,
                        remark = remark,
                        userId = userId!!
                    )

                    Log.d("Entry fragment", "****** Create: $fitnessActivity")
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
                        duration = duration,
                        distance = distance,
                        weight = weight,
                        place = place,
                        remark = remark,
                        userId = userId!!
                    )

                    Log.d("Entry fragment", "****** Edit: $fitnessActivity")
                    editActivity(fitnessActivity)
                    activityClear()
                }
            }
        }

        return binding.root
    }

    private fun showDatePickerDialog() {
        val calender = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, y, m, d ->
                val c = Calendar.getInstance()
                c.set(y, m, d)

                val dateFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
                pickedEntryDate = dateFormat.format(c.time).toString()
                binding.editEntryDate.text = pickedEntryDate
            },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(), { _, hr, min ->

                val c = Calendar.getInstance()
                c.set(Calendar.HOUR_OF_DAY, hr)
                c.set(Calendar.MINUTE, min)

                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                pickedTime = timeFormat.format(c.time).toString()
                binding.editTime.text = pickedTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }

    private fun editActivity(fitnessActivity: FitnessActivity) {
        val url = "http://192.168.100.29:8000/editActivity.php"
        val userId = arguments?.getInt("id")
        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                Log.d("Entry Fragment", "***** Response: $response")
                val obj = JSONObject(response)
                Log.d("Entry Fragment", "***** Json obj: $obj")
                if (obj.get("statusCode") == 200) {
                    Toast.makeText(
                        context,
                        obj.get("message").toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    val action =
                        EntryFragmentDirections.actionEntryFragmentToListVeiwFragment(userId!!)
                    findNavController().navigate(action)
                } else {
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
                    "id" to fitnessActivity.id.toString(),
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
            editEntryDate.text = R.string.enter_entry_date.toString()
            editDay.text.clear()
            editMonth.text.clear()
            editYear.text.clear()
            editTime.text = R.string.enter_time.toString()
            editDuration.text.clear()
            editDistance.text.clear()
            editWeight.text.clear()
            editPlace.text.clear()
            editRemark.text.clear()
        }
    }

    private fun saveActivity(fitnessActivity: FitnessActivity) {
        val url = "http://192.168.100.29:8000/insertActivity.php"
        val userId = arguments?.getInt("id")
        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                Log.d("Entry Fragment", "***** Response: $response")
                val obj = JSONObject(response)
                Log.d("Entry Fragment", "***** Json obj: $obj")
                if (obj.get("statusCode") == 200) {
                    Toast.makeText(
                        context,
                        obj.get("message").toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    val action =
                        EntryFragmentDirections.actionEntryFragmentToListVeiwFragment(userId!!)
                    findNavController().navigate(action)
                } else {
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
