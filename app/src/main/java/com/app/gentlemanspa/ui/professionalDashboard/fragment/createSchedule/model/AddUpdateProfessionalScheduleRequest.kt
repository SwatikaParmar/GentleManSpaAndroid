package com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.model

/*data class AddUpdateProfessionalScheduleRequest(
    val breakFromTime: String,
    val breakToTime: String,
    val professionalDetailId: Int,
    val professionalScheduleId: Int,
    val weekdaysId: Int,
    val fromTime: String,
    val toTime: String
)*/

data class AddUpdateProfessionalScheduleRequest(
    val professionalDetailId: Int,
    val professionalScheduleId: Int,
    val weekdaysId: Int,
    val workingTime: List<WorkingTime>
)

data class WorkingTime(
    val fromTime: String,
    val toTime: String
)