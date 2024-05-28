package ru.internetcloud.workorderapplication.common.presentation.util

fun String.toFloatOrDefault(): Float {
    return if (this.isEmpty()) {
        0f
    } else {
        try {
            this.toFloat()
        } catch (e: Exception) {
            0f
        }
    }
}

fun String.toIntOrDefault(): Int {
    return if (this.isEmpty()) {
        0
    } else {
        try {
            this.toInt()
        } catch (e: Exception) {
            0
        }
    }
}
