package ru.internetcloud.workorderapplication.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.presentation.logon.LogonFragment
import ru.internetcloud.workorderapplication.presentation.synchro.DataSynchronizationFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.WorkOrderFragment
import ru.internetcloud.workorderapplication.presentation.workorder.list.WorkOrderListFragment

class MainActivity :
    AppCompatActivity(),
    WorkOrderListFragment.Callbacks,
    LogonFragment.Callbacks,
    DataSynchronizationFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        if (savedInstanceState == null) {
            val fragment = LogonFragment.newInstance() // фрагмент, который запускается первым
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onAddWorkOrder() {
        val fragment = WorkOrderFragment.newInstanceAddWorkOrder()
        showFragmentWithBackStack(fragment)
    }

    override fun onEditWorkOrder(workOrderId: String) {
        val fragment = WorkOrderFragment.newInstanceEditWorkOrder(workOrderId)
        showFragmentWithBackStack(fragment)
    }

    override fun onLaunchWorkOrderList() {
        val fragment = WorkOrderListFragment.newInstance()
        showFragment(fragment)
    }

    override fun onLaunchDataSynchronization() {
        val fragment = DataSynchronizationFragment.newInstance()
        showFragment(fragment)
    }

    private fun showFragmentWithBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
