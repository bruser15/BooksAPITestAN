package com.example.networktest.model.presentation

data class BookResponse(
    val items: List<BookItem>
)

data class BookItem(
    val volumeInfo: VolumeItem
)

data class VolumeItem(
    val title: String,
    val authors: List<String>
)
