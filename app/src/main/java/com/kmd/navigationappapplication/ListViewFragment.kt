package com.kmd.navigationappapplication

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kmd.navigationappapplication.databinding.FragmentListViewBinding
import org.json.JSONObject

class ListViewFragment : Fragment() {
    private lateinit var binding: FragmentListViewBinding
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListViewBinding.inflate(inflater, container, false)

        userId = arguments?.getInt("id")!!
        Log.d("List View Activity", "***** user id: $userId")
        retrieveActivity(userId)

        return binding.root
    }

    private fun retrieveActivity(userId: Int) {
        val url = "http://192.168.100.29:8000/showActivity.php"
        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                Log.d("List View Fragment", "***** Response: $response")
                val obj = JSONObject(response)
                Log.d("List View Fragment", "***** Response json object: $response")
                if (obj.get("statusCode") == 200) {
                    val activityList = obj.getJSONArray("data")
                    Log.d("List View Fragment", "***** Activity List: $activityList")

                    val activityArrayList = ArrayList<FitnessActivity>()

                    for (i in 0 until activityList.length()) {
                        val activityObj = activityList.getJSONObject(i)
                        val fitnessActivity =
                            FitnessActivity(
                                activityObj.getInt("id"),
                                activityObj.getString("type"),
                                activityObj.getString("entryDate"),
                                activityObj.getInt("day"),
                                activityObj.getInt("month"),
                                activityObj.getInt("year"),
                                activityObj.getString("time"),
                                activityObj.getInt("duration"),
                                activityObj.getDouble("distance"),
                                activityObj.getDouble("weight"),
                                activityObj.getString("place"),
                                activityObj.getString("remark"),
                                activityObj.getInt("userId")
                            )
                        activityArrayList.add(fitnessActivity)
                    }

                    this.showRecyclerView(activityArrayList)
                } else {
                    binding.recyclerViewActivityList.visibility = View.GONE
                    binding.txtNoData.text = "There is no activity in the list."
                    binding.txtNoData.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Log.d("List View Fragment", "***** Error: ${e.message}")
            }
        }, Response.ErrorListener { error ->
            Log.d("List View Fragment", "***** Error: ${error.message}")
        }) {
            override fun getParams(): Map<String?, String?> {
                return mapOf(
                    "userId" to userId.toString()
                )
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    private fun editFitnessActivity(fitnessActivity: FitnessActivity) {
        Log.d("List View Fragment", "***** Working in the edit fitness activity")
        Log.d("List View Fragment", "***** Fitness Activity: $fitnessActivity")

        val action = ListViewFragmentDirections.actionListVeiwFragmentToEntryFragment(userId, fitnessActivity)
        findNavController().navigate(action)
    }

    private fun deleteFitnessActivity(activityId: Int) {
        Log.d("List View Fragment", "***** Working in the delete fitness activity")
        Log.d("List View Fragment", "***** Activity Id: $activityId")

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Confirmation").setMessage("Are you sure want to delete it?")
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                deleteApi(activityId)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showActivityDetails(fitnessActivity: FitnessActivity) {
        Log.d("List View Fragment", "***** Working in the show activity details")
        Log.d("List View Fragment", "***** Fitness Activity: $fitnessActivity")

        val st = StringBuilder()
        st.append("Type: " + fitnessActivity.type + "\n")
        st.append("Entry Date: " + fitnessActivity.entryDate + "\n")
        st.append("Day: " + fitnessActivity.day + "\n")
        st.append("Month: " + fitnessActivity.month + "\n")
        st.append("Year: " + fitnessActivity.year + "\n")
        st.append("Time: " + fitnessActivity.time + "\n")
        st.append("Type: " + fitnessActivity.type + "\n")
        st.append("Duration: " + fitnessActivity.duration + "\n")
        st.append("Distance: " + fitnessActivity.distance + "\n")
        st.append("Weight: " + fitnessActivity.weight + "\n")
        st.append("Place: " + fitnessActivity.place + "\n")
        st.append("Remark: " + fitnessActivity.remark + "\n")

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Activity Details")
            .setMessage(st.toString())
            .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

    }

    private fun deleteApi(activityId: Int) {
        Log.d("List View Fragment", "***** Working in the delete api")
        Log.d("List View Fragment", "***** Activity Id: $activityId")

        val url = "http://192.168.100.29:8000/deleteActivity.php"
        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                Log.d("List View Fragment", "***** Response: $response")
                val obj = JSONObject(response)
                Log.d("List View Fragment", "***** Response Json Object: $obj")

                if (obj.getInt("statusCode") == 200) {
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                    this.retrieveActivity(userId)
                } else {
                    val alertDialog = AlertDialog.Builder(requireContext())
                    alertDialog.setTitle("Sorry").setMessage(obj.getString("message"))
                        .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
            } catch (e: Exception) {
                Log.d("List View Fragment", "***** Error: ${e.message}")
            }
        }, Response.ErrorListener { error ->
            Log.d("List View Fragment", "***** Error: ${error.message}")
        }) {
            override fun getParams(): Map<String?, String?> {
                return mapOf(
                    "activityId" to activityId.toString()
                )
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    private fun showRecyclerView(activityArrayList: ArrayList<FitnessActivity>) {
        binding.recyclerViewActivityList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewActivityList.adapter =
            ActivityListAdapter(
                context,
                activityArrayList,
                onEditHandler = { fitnessActivity ->
                    editFitnessActivity(fitnessActivity)
                },
                onDeleteHandler = { activityId ->
                    deleteFitnessActivity(activityId)
                },
                onShowActivityHandler = { fitnessActivity ->
                    showActivityDetails(fitnessActivity)
                })
        binding.recyclerViewActivityList.adapter?.notifyDataSetChanged()
    }
}
