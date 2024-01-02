package ru.internetcloud.workorderapplication.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.navigationimpl.NavigationActivity
import ru.internetcloud.workorderapplication.navigationimpl.NavigationFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("rustam", "MainActivity")
    }

    override fun getNavigationFragment(): NavigationFragment? = supportFragmentManager.fragments
        .filterIsInstance<NavigationFragment>()
        .firstOrNull()
}
