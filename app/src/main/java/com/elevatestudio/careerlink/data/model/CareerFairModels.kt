package com.elevatestudio.careerlink.data.model

class CareerFairModels {
    data class Booth(
        val id: Int,
        val name: String,
        val company: String,
        val desc: String
    )

    data class Event(
        val id: Int,
        val title: String,
        val date: String,
        val location: String,
        val description: String
    )
}