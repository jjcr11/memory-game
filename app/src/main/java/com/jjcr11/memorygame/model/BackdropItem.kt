package com.jjcr11.memorygame.model

data class BackdropItem(
    val icon: Int,
    val text: String,
    var selected: Boolean,
    val onClick: () -> Unit
)