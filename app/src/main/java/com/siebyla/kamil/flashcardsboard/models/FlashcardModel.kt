package com.siebyla.kamil.flashcardsboard.models

class Flashcard(val uid: String, val title: String, val content: String, val userUid: String) {
    constructor() : this("", "", "", "")
}