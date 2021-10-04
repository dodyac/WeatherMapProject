package com.acxdev.weathermapproject.data.model

class User {
    var _id: Long? = null
    var username: String = ""
    var email: String = ""
    var name: String = ""
    var pin: String = ""
    constructor()

    constructor(
        username: String,
        email: String,
        name: String,
        pin: String,
        _id: Long? = null
    ) {
        this.username = username
        this.email = email
        this.name = name
        this.pin = pin
        this._id = _id
    }
}