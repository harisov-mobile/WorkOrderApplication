package ru.internetcloud.workorderapplication.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.common.MessageDialogMode
import ru.internetcloud.workorderapplication.presentation.workorder.detail.department.DepartmentListAdapter
import ru.internetcloud.workorderapplication.presentation.workorder.detail.department.DepartmentListViewModel
import ru.internetcloud.workorderapplication.presentation.workorder.detail.department.DepartmentPickerFragment

class MessageDialogFragment: DialogFragment() {

    companion object {

        private const val MESSAGE_ARG = "message_arg"
        private const val MODE_ARG = "mode_arg"

        fun newInstance(message: String, messageDialogMode: MessageDialogMode): MessageDialogFragment {
            val args = Bundle().apply {
                putString(MESSAGE_ARG, message)
                putParcelable(MODE_ARG, messageDialogMode)
            }
            return MessageDialogFragment().apply {
                arguments = args
            }
        }
    }

    private var messageDialogMode: MessageDialogMode? = null
    private var message: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let { arg ->
            messageDialogMode = arg.getParcelable(MODE_ARG)
            message = arg.getString(MESSAGE_ARG, "")
        } ?: run {
            throw RuntimeException("There are not arguments in MessageDialogFragment")
        }

        val dialogStyle = when (messageDialogMode) {
            MessageDialogMode.ERROR -> R.style.ErrorDialogTheme
            MessageDialogMode.SUCCESS -> R.style.SuccessDialogTheme
            MessageDialogMode.INFO -> R.style.InfoDialogTheme
            else -> R.style.InfoDialogTheme
        }

        val alertDialogBuilder = AlertDialog.Builder(activity, dialogStyle)
        val alertDialogBuilder = AlertDialog.Builder(activity)
        // alertDialogBuilder.setTitle(R.string.department_picker_title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(R.string.button_ok, null)

        return alertDialogBuilder.create()
    }
}