package com.epam.nyekilajos.archcomppoc.inject

import androidx.lifecycle.*
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class DaggerActivityWithViewModel : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

}

inline fun <reified T : ViewModel> daggerViewModel(activity: DaggerActivityWithViewModel) = DaggerViewModelActivityDelegate(activity, T::class)

class DaggerViewModelActivityDelegate<T : ViewModel>(private val activity: DaggerActivityWithViewModel, private val kClass: KClass<T>) : DefaultLifecycleObserver {

    private var viewModel: T? = null

    init {
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        viewModel = ViewModelProviders.of(activity, activity.viewModelFactory).get(kClass.java)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return viewModel ?: throw IllegalStateException("Activity is not started yet.")
    }
}
