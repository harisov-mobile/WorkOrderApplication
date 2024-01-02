package ru.internetcloud.workorderapplication.workorders.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.buildconfig.BuildConfigFieldsProvider
import ru.internetcloud.workorderapplication.common.domain.common.DateConverter
import ru.internetcloud.workorderapplication.common.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.common.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.common.presentation.dialog.MessageDialogFragment
import ru.internetcloud.workorderapplication.common.presentation.dialog.QuestionDialogFragment
import ru.internetcloud.workorderapplication.common.presentation.util.EditMode
import ru.internetcloud.workorderapplication.common.presentation.util.ReturnResult
import ru.internetcloud.workorderapplication.common.presentation.util.UiState
import ru.internetcloud.workorderapplication.common.presentation.util.launchAndCollectIn
import ru.internetcloud.workorderapplication.navigationapi.NavigationApi
import ru.internetcloud.workorderapplication.workorders.R
import ru.internetcloud.workorderapplication.workorders.databinding.FragmentWorkOrderListBinding
import ru.internetcloud.workorderapplication.workorders.presentation.navigation.ToWorkOrderArgs
import ru.internetcloud.workorderapplication.workorders.presentation.navigation.WorkOrdersDirections
import ru.internetcloud.workorderapplication.workorders.presentation.search.SearchWorkOrderFragment

@AndroidEntryPoint
class WorkOrderListFragment : Fragment(R.layout.fragment_work_order_list), FragmentResultListener {

    private val binding by viewBinding(FragmentWorkOrderListBinding::bind)
    private val viewModel by viewModels<WorkOrderListViewModel>()

    @Inject
    lateinit var buildConfigFieldsProvider: BuildConfigFieldsProvider

    @Inject
    lateinit var navigationApi: NavigationApi<WorkOrdersDirections>

    // согласно моему открытию: Не надо самому вмешиваться в позиционирование списка после удаления!
    // RecyclerView и Adapter сами знают, что надо делать!
    // Надо просто не занулять адаптер
    private val workOrderListAdapter: WorkOrderListAdapter by lazy {
        // сначала - обработчик нажатий на элемент списка:
        val workOrderListListener = object : WorkOrderListListener {

            override fun onItemClick(workOrder: WorkOrder) {
                navigationApi.navigate(
                    WorkOrdersDirections.ToWorkOrder(
                        args = ToWorkOrderArgs(
                            workOrderId = workOrder.id,
                            editMode = EditMode.Edit,
                            requestKeyReturnResult = REQUEST_KEY_RETURN_RESULT,
                            argNameReturnResult = ARG_NAME_RETURN_RESULT
                        )
                    )
                )
            }
        }
        // потом - создаем адаптер, который не будет зануляться при
        // жонглировании фрагментами
        WorkOrderListAdapter(workOrderListListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // адаптер в binding занулять, так как
        //        В книге "Real World" на стр. 133 занулению адаптера посвящен абзац:
        //        Having an Adapter as a property of a Fragment is a known way of
        //        leaking the RecyclerView.
        //        That’s because, when the View is destroyed, the RecyclerView is destroyed
        //        along with it. But if the Fragment references the Adapter, the garbage
        //        collector won’t be able to collect the RecyclerView instance because
        //                Adapter s and RecyclerViews have a circular dependency. In other words,
        //        they reference each other.
        binding.workOrderRecyclerView.adapter = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        setupWorkOrderRecyclerView()
        setupFilterDescription()

        setupClickListeners()
        setupFragmentResultListeners()
        interceptExit() // перехват нажатия кнопки "Back" (задаем вопрос "Do you want to exit the app?")
        observeViewModel()
    }

    private fun setupFragmentResultListeners() {
        // чтобы получать от дочерних диалоговых фрагментов информацию
        childFragmentManager.setFragmentResultListener(REQUEST_KEY_EXIT_QUESTION, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_SEARCH_WO_DATA_PICKER_KEY, viewLifecycleOwner, this)

        // из фрагмента WorkOrderFragment прилетит ответ в виде ReturnResult
        setFragmentResultListener(REQUEST_KEY_RETURN_RESULT) { _, bundle ->
            val returnResult = bundle.getParcelable<ReturnResult>(ARG_NAME_RETURN_RESULT)
            returnResult?.let { returnedResult ->
                viewModel.returnedResult = returnedResult
            } ?: let {
                viewModel.returnedResult = ReturnResult.NoOperation
            }
        }
    }

    private fun setupClickListeners() {
        binding.tryAgainButton.setOnClickListener {
            viewModel.fetchWorkOrders()
        }

        binding.addFab.setOnClickListener {
            // при добавлении нового заказ-наряда надо спозиционироваться на нем
            viewModel.selectedWorkOrder = null

            navigationApi.navigate(
                WorkOrdersDirections.ToWorkOrder(
                    args = ToWorkOrderArgs(
                        workOrderId = WorkOrder.EMPTY_ID,
                        editMode = EditMode.Add,
                        requestKeyReturnResult = REQUEST_KEY_RETURN_RESULT,
                        argNameReturnResult = ARG_NAME_RETURN_RESULT
                    )
                )
            )
        }
    }

    private fun interceptExit() {
        // перехват нажатия кнопки "Back"
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onExitWorkOrderList()
                }
            }
        )
    }

    private fun observeViewModel() {
        viewModel.screenState.launchAndCollectIn(viewLifecycleOwner) { state ->
            when (state) {
                UiState.Loading -> {
                    renderErrorViews(visible = false, errorMessage = null)
                    renderEmptyDataViews(visible = false)
                    renderShopItemListViews(visible = false)
                    renderLoadingViews(visible = true)
                }

                UiState.EmptyData -> {
                    renderErrorViews(visible = false, errorMessage = null)
                    renderShopItemListViews(visible = false)
                    renderLoadingViews(visible = false)
                    renderEmptyDataViews(visible = true)
                }

                is UiState.Error -> {
                    renderShopItemListViews(visible = false)
                    renderEmptyDataViews(visible = false)
                    renderLoadingViews(visible = false)
                    renderErrorViews(visible = true, errorMessage = state.exception.message)
                }

                is UiState.Success -> {
                    workOrderListAdapter.submitList(state.data) {
                        // спозиционироваться на введенном элементе или отредактированном
                        // когда submitList сделает свою работу, тогда itemCount будет выдавать правильное значение
                        // и можно позиционироваться:
                        if (state.isNew) {
                            if (viewModel.scrollDownto) {
                                binding.workOrderRecyclerView.scrollToPosition(workOrderListAdapter.currentList.size - 1)
                                viewModel.scrollDownto = false
                            } else {
                                if (state.isNew && viewModel.returnedResult is ReturnResult.Changed) {
                                    val workOrderId = (viewModel.returnedResult as ReturnResult.Changed).workOrderId
                                    val currentPosition = getPosition(workOrderId, workOrderListAdapter.currentList)
                                    if (currentPosition != NOT_FOUND_POSITION) {
                                        scrollToCurrentPosition(currentPosition)
                                    }
                                    viewModel.returnedResult = ReturnResult.NoOperation
                                }
                            }
                            viewModel.setIsNew(isNew = false)
                        }
                    }
                    renderEmptyDataViews(visible = false)
                    renderErrorViews(visible = false, errorMessage = null)
                    renderLoadingViews(visible = false)
                    renderShopItemListViews(visible = true)
                }
            }
        }
    }

    private fun renderLoadingViews(visible: Boolean) {
        with(binding) {
            progressBar.isVisible = visible
        }
    }

    private fun renderErrorViews(visible: Boolean, errorMessage: String? = null) {
        with(binding) {
            errorTextView.isVisible = visible
            errorTextView.text = errorMessage
            tryAgainButton.isVisible = visible
        }
    }

    private fun renderEmptyDataViews(visible: Boolean) {
        with(binding) {
            emptyDataTextView.isVisible = visible
            addFab.isVisible = visible
        }
    }

    private fun renderShopItemListViews(visible: Boolean) {
        with(binding) {
            workOrderRecyclerView.isVisible = visible
            addFab.isVisible = visible
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.work_order_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_menu_item -> {
                onSearch()
                return true
            }

            R.id.exit_menu_item -> {
                onExitWorkOrderList()
                return true
            }

            R.id.synchronize_data_menu_item -> {
                viewModel.scrollDownto = true
                // запустить фрагмент, где будет синхронизация данных из 1С
                navigationApi.navigate(WorkOrdersDirections.ToDataSynchronization)
                return true
            }

            R.id.settings_menu_item -> {
                MessageDialogFragment.newInstance(
                    getString(R.string.under_constraction)
                )
                    .show(childFragmentManager, null)
                return true
            }

            R.id.about_menu_item -> {
                // запустить фрагмент, где будет о программе
                MessageDialogFragment.newInstance(
                    getString(
                        R.string.about_application,
                        getString(R.string.app_name),
                        buildConfigFieldsProvider.get().versionName
                    )
                )
                    .show(childFragmentManager, null)
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun onSearch() {
        SearchWorkOrderFragment
            .newInstance(
                viewModel.searchWorkOrderData,
                REQUEST_SEARCH_WO_DATA_PICKER_KEY,
                ARG_SEARCH_WO_DATA
            )
            .show(childFragmentManager, REQUEST_SEARCH_WO_DATA_PICKER_KEY)
    }

    private fun setupWorkOrderRecyclerView() {
        binding.workOrderRecyclerView.adapter = workOrderListAdapter
        binding.workOrderRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.workOrderRecyclerView.setHasFixedSize(true)
    }

    private fun onExitWorkOrderList() {
        QuestionDialogFragment
            .newInstance(
                getString(R.string.exit_from_app_question, getString(R.string.app_name)),
                REQUEST_KEY_EXIT_QUESTION,
                ARG_NAME_EXIT_QUESTION
            )
            .show(childFragmentManager, REQUEST_KEY_EXIT_QUESTION)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            // ответ на вопрос: "Выйти из приложения?"
            REQUEST_KEY_EXIT_QUESTION -> {
                val exit: Boolean = result.getBoolean(ARG_NAME_EXIT_QUESTION, false)
                if (exit) {
                    activity?.finish()
                }
            }

            REQUEST_SEARCH_WO_DATA_PICKER_KEY -> {
                val searchData: SearchWorkOrderData? = result.getParcelable(ARG_SEARCH_WO_DATA)
                searchData?.let { data ->
                    if (data != viewModel.searchWorkOrderData) {
                        viewModel.searchWorkOrderData = data
                        viewModel.selectedWorkOrder = null
                        viewModel.scrollDownto = true
                        setupFilterDescription()
                        viewModel.fetchWorkOrders()
                    }
                }
            }
        }
    }

    private fun setupFilterDescription() {
        with(binding) {
            if (viewModel.searchWorkOrderDataIsEmpty()) {
                // пользователь сбросил отбор
                filterDescriptionTextView.text = ""
                filterDescriptionTextView.visibility = View.GONE
            } else {
                // пользователь установил отбор
                filterDescriptionTextView.text = getFilterDescription(viewModel.searchWorkOrderData)
                filterDescriptionTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun getFilterDescription(searchWorkOrderData: SearchWorkOrderData): String {
        var description = ""

        if (searchWorkOrderData.numberText.isNotEmpty()) {
            description = description + getString(R.string.number_hint) +
                    " " + getString(R.string.contains) + " " + searchWorkOrderData.numberText + "; "
        }

        if (searchWorkOrderData.partnerText.isNotEmpty()) {
            description = description + getString(R.string.partner_hint) +
                    " " + getString(R.string.contains) + " " + searchWorkOrderData.partnerText + "; "
        }

        if (searchWorkOrderData.carText.isNotEmpty()) {
            description = description + getString(R.string.car_hint) +
                    " " + getString(R.string.contains) + " " + searchWorkOrderData.carText + "; "
        }

        if (searchWorkOrderData.performerText.isNotEmpty()) {
            description = description + getString(R.string.performer_hint) +
                    " " + getString(R.string.contains) + " " + searchWorkOrderData.performerText + "; "
        }

        if (searchWorkOrderData.departmentText.isNotEmpty()) {
            description = description + getString(R.string.department_hint) +
                    " " + getString(R.string.contains) + " " + searchWorkOrderData.departmentText + "; "
        }

        searchWorkOrderData.dateFrom?.let { fromDate ->
            description = description + getString(R.string.date_from_hint) +
                    " " + DateConverter.getDateString(fromDate) + "; "
        }

        searchWorkOrderData.dateTo?.let { toDate ->
            description = description + getString(R.string.date_to_hint) +
                    " " + DateConverter.getDateString(toDate) + "; "
        }

        if (description.isNotEmpty()) {
            description = getString(R.string.filter_fragment_title) + ": " + description
        }

        return description
    }

    private fun getPosition(id: String, list: List<WorkOrder>): Int {
        var isFound = false

        // надо идти с конца списка
        val listSize = list.size
        var currentPosition = listSize // в цикле будет уменьшение

        for (i in listSize downTo 1) {
            currentPosition-- // уменьшение
            if (list.get(currentPosition).id == id) {
                isFound = true
                break
            }
        }
        if (!isFound) {
            currentPosition = NOT_FOUND_POSITION
        }
        return currentPosition
    }

    private fun scrollToCurrentPosition(currentPosition: Int) {
        val itemCount = workOrderListAdapter.getItemCount()
        val scrollPosition = if (currentPosition > (itemCount - DIFFERENCE_POS)) {
            itemCount - 1
        } else {
            currentPosition
        }
        binding.workOrderRecyclerView.scrollToPosition(scrollPosition)
    }

    companion object {
        // эти константы нужны для диалогового окна - "Выйти из приложения?"
        private val REQUEST_KEY_EXIT_QUESTION = "request_key_exit_question"
        private val ARG_NAME_EXIT_QUESTION = "arg_name_exit_question"

        // эти константы нужны для получения от детального фрагмента - надо ли делать скролл
        private const val REQUEST_KEY_RETURN_RESULT = "key_return_result"
        private const val ARG_NAME_RETURN_RESULT = "arg_name_return_result"

        private const val REQUEST_SEARCH_WO_DATA_PICKER_KEY = "request_search_wo_data_picker_key"
        private const val ARG_SEARCH_WO_DATA = "search_wo_data_picker"

        private const val DIFFERENCE_POS = 5
        private const val NOT_FOUND_POSITION = -1
    }
}
