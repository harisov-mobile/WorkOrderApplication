package ru.internetcloud.workorderapplication.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.internetcloud.workorderapplication.R

class MessageDialogFragment : DialogFragment() {

    private var message: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let { arg ->
            message = arg.getString(MESSAGE_ARG, "")
        } ?: run {
            throw RuntimeException("There are not arguments in MessageDialogFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(R.string.ok_button, null)

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
