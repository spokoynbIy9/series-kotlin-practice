package com.example.series_kotlin_hm.presentation.profile.model.state

import android.net.Uri

interface ProfileState {
    val name: String
    val photoUri: Uri
}