package ru.internetcloud.workorderapplication.presentation.synchro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.internetcloud.workorderapplication.databinding.FragmentDataSynchronizationBinding

class DataSynchronizationFragment : Fragment() {

    private var _binding: FragmentDataSynchronizationBinding? = null
    private val binding: FragmentDataSynchronizationBinding
        get() = _binding ?: throw RuntimeException("Error DataSynchronizationFragmentBinding is NULL")

    private lateinit var viewModel: DataSynchronizationFragmentViewModel

    companion object {
        fun newInstance(): DataSynchronizationFragment {
            return DataSynchronizationFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDataSynchronizationBinding.inflate(inflater, container, false)
        return binding.root
    }
}
