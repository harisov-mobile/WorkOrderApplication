package ru.internetcloud.workorderapplication.presentation.synchro

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.databinding.FragmentDataSynchronizationBinding

class DataSynchronizationFragment : Fragment() {

    // интерфейс обратного вызова
    interface Callbacks {
        fun onLaunchWorkOrderList()
    }

    private var hostActivity: DataSynchronizationFragment.Callbacks? = null
    private lateinit var viewModel: DataSynchronizationFragmentViewModel

    private var _binding: FragmentDataSynchronizationBinding? = null
    private val binding: FragmentDataSynchronizationBinding
        get() = _binding ?: throw RuntimeException("Error DataSynchronizationFragmentBinding is NULL")

    companion object {
        fun newInstance(): DataSynchronizationFragment {
            return DataSynchronizationFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hostActivity = context as DataSynchronizationFragment.Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDataSynchronizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DataSynchronizationFragmentViewModel::class.java)

        binding.okButton.setOnClickListener {
            hostActivity?.onLaunchWorkOrderList() // запустить фрагмент, где будет проверка пароля, сихнронизация данных из 1С и т.д.
        }

        binding.exitButton.setOnClickListener {
            activity?.onBackPressed() // это аналог finish для фрагмента
        }

        observeViewModel()

        viewModel.synchonizeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        hostActivity = null
    }

    private fun observeViewModel() {
        // подписка на ошибку:
        viewModel.errorSynchronization.observe(viewLifecycleOwner) {
            if (it) {
                context?.let { currentContext ->
                    binding.progressBar.visibility = View.GONE
                    binding.exitButton.visibility = View.VISIBLE

                    binding.synchroResultTextView.text = getString(R.string.fail_synchronization)
                    binding.synchroResultTextView.setTextColor(Color.RED)
                }
            }
        }

        // подписка на завершение экрана:
        viewModel.canContinue.observe(viewLifecycleOwner) {
            if (it) {
                context?.let { currentContext ->
                    binding.progressBar.visibility = View.GONE
                    binding.okButton.visibility = View.VISIBLE

                    binding.synchroResultTextView.text = getString(R.string.success_synchronization)
                    context?.let { currentContext ->
                        binding.synchroResultTextView.setTextColor(ContextCompat.getColor(currentContext, R.color.dark_green))
                    }
                }
            }
        }

        viewModel.canContinueWithoutSynchro.observe(viewLifecycleOwner) {
            if (it) {
                context?.let { currentContext ->
                    binding.progressBar.visibility = View.GONE
                    binding.okButton.visibility = View.VISIBLE

                    binding.synchroResultTextView.text = getString(R.string.success_autonomus)
                    context?.let { currentContext ->
                        binding.synchroResultTextView.setTextColor(ContextCompat.getColor(currentContext, R.color.dark_yellow))
                    }
                }
            }
        }
    }
}
