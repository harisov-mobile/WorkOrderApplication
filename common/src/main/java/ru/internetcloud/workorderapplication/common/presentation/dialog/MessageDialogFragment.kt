package ru.internetcloud.workorderapplication.common.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MessageDialogFragment : DialogFragment() {

    private var message: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let { arg ->
            message = arg.getString(MESSAGE_ARG, "")
        } ?: run {
            throw RuntimeException("There are not arguments in MessageDialogFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(
            requireContext(),
            ru.internetcloud.workorderapplication.core.brandbook.R.style.CustomAlertDialogSmallCorners
        )
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(
            ru.internetcloud.workorderapplication.common.R.string.ok_button,
            null)

        return alertDialogBuilder.create()
    }

    companion object {

        private const val MESSAGE_ARG = "message_arg"

        fun newInstance(message: String): MessageDialogFragment {
            val args = Bundle().apply {
                putString(MESSAGE_ARG, message)
            }
            return MessageDialogFragment().apply {
                arguments = args
            }
        }
    }
}
