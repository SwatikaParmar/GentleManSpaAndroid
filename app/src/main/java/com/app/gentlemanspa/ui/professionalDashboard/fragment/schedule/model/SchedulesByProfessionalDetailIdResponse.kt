package com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model

data class SchedulesByProfessionalDetailIdResponse(
    val data: List<SchedulesByProfessionalDetailData>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class SchedulesByProfessionalDetailData(
    val breakFromTime: Any,
    val breakToTime: Any,
    val fromTime: String,
    val professionalDetailId: Int,
    val professionalScheduleId: Int,
    val toTime: String,
    val weekdaysId: Int
)