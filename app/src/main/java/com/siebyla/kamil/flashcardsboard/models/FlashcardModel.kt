package com.siebyla.kamil.flashcardsboard.models

class Flashcard (val userId: String, val uid: String, val title: String, val content: String){
    constructor() : this("", "", "", "")
}

