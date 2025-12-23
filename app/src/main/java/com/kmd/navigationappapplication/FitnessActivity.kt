package com.kmd.navigationappapplication

import java.io.Serializable

data class FitnessActivity(
    val id: Int,
    val type: String,
    val entryDate: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val time: String,
    val duration: Int,
    val distance: Double,
    val weight: Double,
    val place: String,
    val remark: String,
    val userId: Int
) : Serializable
