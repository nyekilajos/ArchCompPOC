package com.epam.nyekilajos.archcomppoc.inject

import androidx.lifecycle.*
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class DaggerFragmentWithViewModel : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

}

inline fun <reified T : ViewModel> daggerViewModel(fragment: DaggerFragmentWithViewModel) = DaggerViewModelDelegate(fragment, T::class)

class DaggerViewModelDelegate<T : ViewModel>(private val fragment: DaggerFragmentWithViewModel, private val kClass: KClass<T>) : DefaultLifecycleObserver {

    private var viewModel: T? = null

    init {
        fragment.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        viewModel = ViewModelProviders.of(fragment, fragment.viewModelFactory).get(kClass.java)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return viewModel ?: throw IllegalStateException("Activity is not started yet.")
    }
}
