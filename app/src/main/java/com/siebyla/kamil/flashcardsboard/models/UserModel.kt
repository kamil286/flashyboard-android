package com.siebyla.kamil.flashcardsboard.models

class User(val uid: String, val username: String, val profileImageUrl: String) {
    constructor() : this("", "", "")
}