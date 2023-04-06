package ru.internetcloud.workorderapplication.presentation.workorder.detail.partner

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.WorkOrderApp
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.di.ViewModelFactory

class PartnerPickerFragment : DialogFragment() {

    // даггер:
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WorkOrderApp).component
    }

    private var requestKey = ""
    private var argPartnerName = ""

    private lateinit var viewModel: PartnerListViewModel
    private lateinit var partnerListRecyclerView: RecyclerView
    private lateinit var partnerListAdapter: PartnerListAdapter

    private lateinit var clearSearchTextImageButton: ImageButton
    private lateinit var searchEditText: EditText
    private lateinit var titleTextView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // даггер:
        component.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PartnerListViewModel::class.java)

        arguments?.let { arg ->
            viewModel.selectedPartner ?: let {
                viewModel.selectedPartner = arg.getParcelable(PARTNER)
            }
            requestKey = arg.getString(PARENT_REQUEST_KEY, "")
            argPartnerName = arg.getString(PARENT_PARTNER_ARG_NAME, "")
        } ?: run {
            throw RuntimeException("There are not arguments in PartnerPickerFragment")
        }

        val alertDialogBuilder = AlertDialog.Builder(activity)
        // alertDialogBuilder.setTitle(R.string.partner_picker_title)

        val container = layoutInflater.inflate(R.layout.fragment_picker, null, false)
        clearSearchTextImageButton = container.findViewById(R.id.clear_search_text_button)
        searchEditText = container.findViewById(R.id.search_edit_text)

        titleTextView = container.findViewById(R.id.title_text_view)
        titleTextView.text = getString(R.string.partner_picker_title)

        alertDialogBuilder.setView(container)

        alertDialogBuilder.setNeutralButton(R.string.clear_button) { _, _ ->
            sendResultToFragment(null)
        }

        alertDialogBuilder.setNegativeButton(R.string.cancel_button, null) // для негативного ответа ничего не делаем

        alertDialogBuilder.setPositiveButton(R.string.ok_button) { dialog, which ->
            sendResultToFragment(viewModel.selectedPartner)
        }

        setupPartnerListRecyclerView(container)

        viewModel.partnerListLiveData.observe(this, { partners ->
            partnerListAdapter = PartnerListAdapter(partners)
            partnerListRecyclerView.adapter = partnerListAdapter
            setupClickListeners()

            val currentPosition = getPosition(viewModel.selectedPartner, partnerListAdapter.partners)

            if (currentPosition == NOT_FOUND_POSITION) {
                viewModel.selectedPartner = null
            } else {
                viewModel.selectedPartner = partners[currentPosition]
                viewModel.selectedPartner?.isSelected = true

                val scrollPosition = if (currentPosition > (partnerListAdapter.getItemCount() - DIFFERENCE_POS)) {
                    partnerListAdapter.getItemCount() - 1
                } else {
                    currentPosition
                }

                partnerListRecyclerView.scrollToPosition(scrollPosition)
                partnerListAdapter.notifyItemChanged(currentPosition, Unit)
            }
        })

        savedInstanceState ?: let {
            viewModel.loadPartnerList() // самое главное!!! если это создание нового фрагмента
        }

        return alertDialogBuilder.create()
    }

    private fun setupPartnerListRecyclerView(view: View) {
        partnerListRecyclerView = view.findViewById(R.id.list_recycler_view)
        partnerListAdapter = PartnerListAdapter(emptyList())
        // LinearLayoutManager задан в XML-разметке
        partnerListRecyclerView.adapter = partnerListAdapter
    }

    override fun onStart() {
        super.onStart()

        // TextWatcher нужно навешивать здесь, а не в onCreate или onCreateView, т.к. там еще не восстановлено
        // EditText и слушатели будут "дергаться" лишний раз
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // viewModel.resetErrorInputNumber()
            }

            override fun afterTextChanged(p0: Editable?) {
                search(p0?.toString() ?: "")
            }
        })
    }

    private fun setupClickListeners() {
        partnerListAdapter.onPartnerClickListener = { currentPartner ->
            viewModel.selectedPartner?.isSelected = false // предыдущий отмеченный - снимаем пометку
            viewModel.selectedPartner = currentPartner
            viewModel.selectedPartner?.isSelected = true
        }

        partnerListAdapter.onPartnerLongClickListener = { currentPartner ->
            sendResultToFragment(currentPartner)
            dismiss()
        }

        clearSearchTextImageButton.setOnClickListener {
            searchEditText.setText("")
            viewModel.loadPartnerList()
        }
    }

    private fun search(searchText: String) {
        if (searchText.isEmpty()) {
            viewModel.loadPartnerList()
        } else {
            viewModel.searchPartners(searchText)
        }
    }

    private fun getPosition(searchedPartner: Partner?, partnerList: List<Partner>): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedPartner?.let {
            var isFound = false
            for (currentPartner in partnerList) {
                currentPosition++
                if (currentPartner.id == searchedPartner.id) {
                    isFound = true
                    break
                }
            }
            if (!isFound) {
                currentPosition = NOT_FOUND_POSITION
            }
        }
        return currentPosition
    }

    private fun sendResultToFragment(result: Partner?) {
        val bundle = Bundle().apply {
            putParcelable(argPartnerName, result)
        }
        setFragmentResult(requestKey, bundle)
    }

    companion object {

        private const val PARTNER = "partner"
        private const val PARENT_REQUEST_KEY = "parent_request_partner_picker_key"
        private const val PARENT_PARTNER_ARG_NAME = "parent_partner_arg_name"

        private const val NOT_FOUND_POSITION = -1
        private const val DIFFERENCE_POS = 5

        fun newInstance(partner: Partner?, parentRequestKey: String, parentArgDateName: String): PartnerPickerFragment {
            val args = Bundle().apply {
                putParcelable(PARTNER, partner)
                putString(PARENT_REQUEST_KEY, parentRequestKey)
                putString(PARENT_PARTNER_ARG_NAME, parentArgDateName)
            }
            return PartnerPickerFragment().apply {
                arguments = args
            }
        }
    }
}
