package com.epam.nyekilajos.archcomppoc.inject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class DaggerAwareViewModelFactory @Inject constructor(
        private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return (creators[modelClass]
                ?: creators.entries
                        .find { modelClass.isAssignableFrom(it.key) }
                        ?.value)
                ?.get() as? T
                ?: throw IllegalArgumentException("Unknown model class $modelClass")
    }
}
