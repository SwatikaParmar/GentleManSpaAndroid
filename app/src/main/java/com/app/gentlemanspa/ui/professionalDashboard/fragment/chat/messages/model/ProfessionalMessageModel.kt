package com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.messages.model


class ProfessionalMessageModel {
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
