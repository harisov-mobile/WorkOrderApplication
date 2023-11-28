package ru.internetcloud.workorderapplication.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.internetcloud.workorderapplication.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

//class MainActivity :
//    AppCompatActivity(),
//    WorkOrderListFragment.Callbacks,
//    LoginFragment.Callbacks,
//    DataSynchronizationFragment.Callbacks {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_fragment)
//
//        if (savedInstanceState == null) {
//            val fragment = LoginFragment.newInstance() // фрагмент, который запускается первым
//            supportFragmentManager.beginTransaction()
//                .add(R.id.fragment_container, fragment)
//                .commit()
//        }
//    }
//
//
//
//    override fun onLaunchWorkOrderList() {
//        val fragment = WorkOrderListFragment.newInstance()
//        showFragment(fragment)
//    }
//
//
//    private fun showFragmentWithBackStack(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .addToBackStack(null)
//            .commit()
//    }
//
//    private fun showFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .commit()
//    }
//}
