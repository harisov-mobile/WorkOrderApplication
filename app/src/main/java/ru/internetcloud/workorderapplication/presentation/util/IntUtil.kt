package ru.internetcloud.workorderapplication.presentation.util

fun Int?.orDefault(): Int = this ?: 0

fun Int.convertToString(): String {
    return if (this == 0) {
        ""
    } else {
        this.toString()
    }
}
