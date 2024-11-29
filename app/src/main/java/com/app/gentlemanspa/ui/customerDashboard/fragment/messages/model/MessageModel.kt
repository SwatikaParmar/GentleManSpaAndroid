package com.app.gentlemanspa.ui.customerDashboard.fragment.messages.model

/*
data class MessagesModel(
    var name:String,
    var email:String,
    var uid:String,
)*/

class MessageModel {
    var name: String? = null
    var status: String? = null
    var image: String? = null

    constructor() {}
    constructor(name: String?, status: String?, image: String?) {
        this.name = name
        this.status = status
        this.image = image
    }
}
