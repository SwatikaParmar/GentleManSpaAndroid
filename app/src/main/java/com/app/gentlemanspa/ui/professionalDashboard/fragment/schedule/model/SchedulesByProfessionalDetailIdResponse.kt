package com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model

data class SchedulesByProfessionalDetailIdResponse(
    val `data`: List<SchedulesByProfessionalDetailData>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class SchedulesByProfessionalDetailData(
    val breakTime: List<Any>,
    val professionalDetailId: Int,
    val professionalScheduleId: Int,
    val weekdaysId: Int,
    val weekName: String,
    val workingTime: List<SchedulesByProfessionalDetailWorkingTime>
)

data class SchedulesByProfessionalDetailWorkingTime(
    val fromTime: String,
    val toTime: String
)