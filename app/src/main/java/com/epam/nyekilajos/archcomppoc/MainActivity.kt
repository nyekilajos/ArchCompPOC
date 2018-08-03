package com.epam.nyekilajos.archcomppoc

import android.os.Bundle
import com.epam.nyekilajos.archcomppoc.ui.adresslist.AddressListFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, AddressListFragment.newInstance())
                    .commitNow()
        }
    }

}
