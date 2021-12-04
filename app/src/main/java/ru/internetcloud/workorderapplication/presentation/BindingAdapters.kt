package ru.internetcloud.workorderapplication.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

@BindingAdapter("errorInputNumber")
fun bindErrorInputNumber(textInputLayout: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        textInputLayout.context.getString(R.string.error_input_number)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("workOrderPresentation")
fun bindWorkOrderPresentation(textView: TextView, workOrder: WorkOrder) {
    textView.text = String.format(
        textView.context.getString(R.string.work_order_presentation),
        workOrder.number,
        workOrder.date.toString()
    )
}