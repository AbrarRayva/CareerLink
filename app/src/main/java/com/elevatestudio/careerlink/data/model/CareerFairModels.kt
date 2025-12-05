package com.elevatestudio.careerlink.data.model

class CareerFairModels {

    // EVENT MODEL
    data class Event(
        val id: Int,
        val title: String,
        val date: String,
        val location: String,
        val description: String,

        // tambahan untuk kebutuhan fitur
        val shortDesc: String = "",
        val bannerUrl: String? = null
    )

    // BOOTH MODEL
    data class Booth(
        val id: Int,
        val name: String,
        val company: String,
        val desc: String,

        // tambahan untuk map booth custom
        val mapX: Float = 0.3f,
        val mapY: Float = 0.3f,

        // relasi ke event mana booth ini berada
        val eventId: Int = 0
    )

    // USER SAVED EVENT
    data class SavedEvent(
        val id: Int,
        val eventId: Int,
        val savedAt: String
    )

    // CHECK IN
    data class CheckIn(
        val id: Int,
        val userId: Int,
        val eventId: Int,
        val checkInTime: String
    )

    // NOTIFICATION
    data class Notification(
        val id: Int,
        val userId: Int,
        val title: String,
        val message: String,
        val createdAtTime: String,
        val isRead: Boolean = false
    )
}
