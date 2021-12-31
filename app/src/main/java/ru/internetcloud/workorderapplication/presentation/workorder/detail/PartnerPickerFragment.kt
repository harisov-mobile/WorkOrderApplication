package ru.internetcloud.workorderapplication.presentation.workorder.detail

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import ru.internetcloud.workorderapplication.R
import java.lang.RuntimeException
import java.util.*

class PartnerPickerFragment: DialogFragment() {

    companion object {

        private const val ARG_PARTNER_ID = "partner_id"
        private const val ARG_PARENT_REQUEST_KEY = "parent_request_partner_id_picker_key"
        private const val ARG_PARENT_ARG_PARTNER_ID_NAME = "parent_arg_partner_id_name"

        fun newInstance(partnerId: String, parentRequestKey: String, parentArgDateName: String): PartnerPickerFragment {
            val args = Bundle().apply {
                putString(ARG_PARTNER_ID, partnerId)
                putString(ARG_PARENT_REQUEST_KEY, parentRequestKey)
                putString(ARG_PARENT_ARG_PARTNER_ID_NAME, parentArgDateName)
            }
            return PartnerPickerFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var partnerId = ""
        var requestKey = ""
        var argPartnerIdName = ""

        arguments?.let { arg ->
            partnerId = arg.getString(ARG_PARTNER_ID, "")
            requestKey = arg.getString(ARG_PARENT_REQUEST_KEY, "")
            argPartnerIdName = arg.getString(ARG_PARENT_ARG_PARTNER_ID_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in PartnerPickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(R.string.partner_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_partner_picker, null, false)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNegativeButton(R.string.button_cancel, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.button_ok
        ) { dialog, which ->
            val bundle = Bundle().apply {
                putString(argPartnerIdName, partnerId)
            }
            Toast.makeText(context, "привет", Toast.LENGTH_SHORT).show()
            setFragmentResult(requestKey, bundle)
        }

        return alertDialogBuilder.create()
    }
}