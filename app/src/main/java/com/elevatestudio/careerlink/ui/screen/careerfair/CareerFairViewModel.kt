package com.elevatestudio.careerlink.ui.screen.careerfair

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.elevatestudio.careerlink.data.model.CareerFairModels.Event

class CareerFairViewModel : ViewModel() {

    val savedEvents = mutableStateListOf<Event>()

    fun isEventSaved(eventId: Int): Boolean {
        return savedEvents.any { it.id == eventId }
    }

    fun saveEvent(event: Event) {
        if (savedEvents.none { it.id == event.id }) {
            savedEvents.add(event)
        }
    }

    fun removeEvent(eventId: Int) {
        savedEvents.removeAll { it.id == eventId }
    }
}
