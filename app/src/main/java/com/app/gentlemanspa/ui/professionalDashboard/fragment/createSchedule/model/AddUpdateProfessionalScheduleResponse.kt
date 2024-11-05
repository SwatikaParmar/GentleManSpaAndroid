package com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.model

data class AddUpdateProfessionalScheduleResponse(
    val `data`: Data,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class Data(
    val breakFromTime: Any,
    val breakToTime: Any,
    val fromTime: String,
    val professionalDetail: Any,
    val professionalDetailId: Int,
    val professionalScheduleId: Int,
    val toTime: String,
    val weekdays: Any,
    val weekdaysId: Int
)