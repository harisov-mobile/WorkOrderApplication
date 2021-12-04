package ru.internetcloud.workorderapplication.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import ru.internetcloud.workorderapplication.R

@BindingAdapter("errorInputNumber")
fun bindErrorInputNumber(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_number)
    } else {
        null
    }
    textInputLayout.error = message
}
