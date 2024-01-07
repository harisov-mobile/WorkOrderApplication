package ru.internetcloud.workorderapplication.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.navigationimpl.NavigationActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun getNavController(): NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        return navHostFragment.navController
    }
}
