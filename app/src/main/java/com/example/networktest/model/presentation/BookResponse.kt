package com.example.networktest.model.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
Serializable - defined in java.lang
        - not customizable
        - uses reflection to decompose/recreate object
        - a lot of temporal objects
Parcelable - defined in Android framework
        - customizable, can choose fields of parcelable
        - does not use reflection, (faster)

Marshall and UnMarshall

 */

@Parcelize
data class BookResponse(
    val items: List<BookItem>
): Parcelable

@Parcelize
data class BookItem(
    val volumeInfo: VolumeItem
): Parcelable

@Parcelize
data class VolumeItem(
    val title: String,
    val authors: List<String>,
    val imageLinks : ImageItem?,
    val description: String?
): Parcelable

@Parcelize
data class ImageItem(
    val thumbnail: String?,
    val small: String?
): Parcelable
