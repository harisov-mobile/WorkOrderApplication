package ru.internetcloud.workorderapplication.presentation.sendemail

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import ru.internetcloud.workorderapplication.R

class SendWorkOrderByIdToEmailDialogFragment : DialogFragment() {

    private lateinit var viewModel: SendWorkOrderByIdToEmailViewModel
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var synchroResultTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var progressBar: ProgressBar


    companion object {

        private const val ORDER_ID = "order_id"
        private const val EMAIL = "email"

        fun newInstance(id: String, email: String): SendWorkOrderByIdToEmailDialogFragment {
            val args = Bundle().apply {
                putString(ORDER_ID, id)
                putString(EMAIL, email)
            }
            return SendWorkOrderByIdToEmailDialogFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        viewModel = ViewModelProvider(this).get(SendWorkOrderByIdToEmailViewModel::class.java)

        arguments?.let { arg ->
            savedInstanceState ?: let {
                viewModel.id = arg.getString(ORDER_ID, "") ?: throw RuntimeException("Id can not be NULL.")
                viewModel.email = arg.getString(EMAIL, "") ?: throw RuntimeException("Email can not be NULL.")
            }

        } ?: run {
            throw RuntimeException("There are not arguments in SendWorkOrderByIdToEmailDialogFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)

        val container = layoutInflater.inflate(R.layout.fragment_send_work_order_to_email, null, false)
        okButton = container.findViewById(R.id.ok_button)
        cancelButton = container.findViewById(R.id.cancel_button)
        synchroResultTextView = container.findViewById(R.id.synchro_result_text_view)
        emailTextView = container.findViewById(R.id.email_text_view)
        progressBar = container.findViewById(R.id.progressBar)

        alertDialogBuilder.setView(container)

        setupViews()

        okButton.visibility = View.INVISIBLE

        setupClickListeners()

        observeViewModel()

        viewModel.uploadWorkOrderById(viewModel.id)

        return alertDialogBuilder.create()
    }

    private fun setupViews() {
        synchroResultTextView.text = getString(R.string.wait)
        emailTextView.text = viewModel.email
    }

    private fun setupClickListeners() {
        // Ok
        okButton.setOnClickListener {
            dialog?.cancel()
        }

        // Cancel
        cancelButton.setOnClickListener {
            dialog?.cancel()
        }
    }

    private fun observeViewModel() {
        viewModel.currentSituation.observe(this) { currentInt ->
            if (currentInt > 0) {
                var errorText = ""
                viewModel.errorMessage.value?.let {
                    errorText = it
                }
                synchroResultTextView.text = getString(currentInt) + " " + errorText
            } else {
                synchroResultTextView.text = ""
            }
        }

        viewModel.canContinue.observe(this) {
            if (it) {
                progressBar.visibility = View.INVISIBLE
                okButton.visibility = View.VISIBLE
                cancelButton.visibility = View.INVISIBLE
            }
        }
    }
}
