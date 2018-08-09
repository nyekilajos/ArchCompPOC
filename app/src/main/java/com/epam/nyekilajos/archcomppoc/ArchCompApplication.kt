package com.epam.nyekilajos.archcomppoc

import com.epam.nyekilajos.archcomppoc.inject.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class ArchCompApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder().create(this)
}
