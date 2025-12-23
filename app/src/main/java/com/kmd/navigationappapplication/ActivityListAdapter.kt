package com.kmd.navigationappapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kmd.navigationappapplication.databinding.ItemActivityListLayoutBinding

class ActivityListAdapter(
    val context: Context?,
    val activityList: ArrayList<FitnessActivity>,
    val onEditHandler: (FitnessActivity) -> Unit,
    val onDeleteHandler: (activityId: Int) -> Unit,
    val onShowActivityHandler: (FitnessActivity) -> Unit
) :
    RecyclerView.Adapter<ActivityListAdapter.ActivityListViewHolder>() {
    class ActivityListViewHolder(val binding: ItemActivityListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivityListViewHolder {
        val binding = ItemActivityListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActivityListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ActivityListViewHolder,
        position: Int
    ) {
        val activity = activityList[position]

        holder.binding.apply {
            txtActivityType.text = activity.type
            txtActivityEntryDate.text = activity.entryDate
            txtActivityDuration.text = activity.duration.toString() + " minutes"

            btnEditActivityItem.setOnClickListener { onEditHandler(activity) }
            btnDetailActivityItem.setOnClickListener { onShowActivityHandler(activity) }
            btnDeleteActivityItem.setOnClickListener { onDeleteHandler(activity.id) }
        }
    }

    override fun getItemCount(): Int {
        return activityList.size
    }
}
