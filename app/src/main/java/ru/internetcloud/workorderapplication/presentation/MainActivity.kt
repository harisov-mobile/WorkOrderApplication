package ru.internetcloud.workorderapplication.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.presentation.logon.LogonFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.WorkOrderFragment
import ru.internetcloud.workorderapplication.presentation.workorder.list.WorkOrderListFragment

class MainActivity : AppCompatActivity(),
    WorkOrderListFragment.Callbacks,
    LogonFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        var currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            //currentFragment = WorkOrderListFragment.newInstance()
            currentFragment = LogonFragment.newInstance() // фрагмент, который запускается первым
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, currentFragment)
                .commit()
        }
    }

    override fun onAddWorkOrder() {
        val fragment = WorkOrderFragment.newInstanceAddWorkOrder()
        showFragment(fragment)
    }

    override fun onEditWorkOrder(workOrderId: Int) {
        val fragment = WorkOrderFragment.newInstanceEditWorkOrder(workOrderId)
        showFragment(fragment)
    }

    override fun onLaunchWorkOrderList() {
        val fragment = WorkOrderListFragment.newInstance()
        showFragment(fragment)
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
