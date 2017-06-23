package ru.bupyc9.demotv.model

import java.net.URI
import java.net.URISyntaxException

data class Movie(val id: Int, val title: String, val studio: String, val imageUrl: String) {
    fun getImageURI(): URI? {
        try {
            return URI(imageUrl)
        } catch (e: URISyntaxException) {
            return null
        }
    }
}