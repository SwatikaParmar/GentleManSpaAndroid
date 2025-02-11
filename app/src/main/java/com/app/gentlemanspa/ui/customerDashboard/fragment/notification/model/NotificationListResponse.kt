package com.app.gentlemanspa.ui.customerDashboard.fragment.notification.model

data class NotificationListResponse(
    val `data`: NotificationListData,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class NotificationListData(
    val currentPage: Int,
    val dataList: List<NotificationListDataItem>,
    val nextPage: String,
    val pageSize: Int,
    val previousPage: String,
    val searchQuery: String,
    val totalCount: Int,
    val totalPages: Int
)

data class NotificationListDataItem(
    val createDate: String,
    val description: String,
    val isNotificationRead: Boolean,
    val notificationSentId: Int,
    val notificationType: String,
    val title: String,
    val userId: String
)