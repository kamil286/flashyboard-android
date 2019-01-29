package com.siebyla.kamil.flashcardsboard.models

class Flashcard {

    companion object Factory {
        fun create (): Flashcard = Flashcard()
    }

    var objectId: String? = null
    var title: String? = null
    var content: String? = null
    var profileImageUrl: String? = null
}