 package ru.internetcloud.workorderapplication.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.internetcloud.workorderapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        var currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            currentFragment = WorkOrderListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, currentFragment)
                .commit()
        }
    }
}
