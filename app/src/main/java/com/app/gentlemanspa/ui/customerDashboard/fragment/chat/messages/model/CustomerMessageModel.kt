package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.model

class CustomerMessageModel {
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