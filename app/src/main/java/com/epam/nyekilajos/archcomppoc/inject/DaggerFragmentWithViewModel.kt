package com.epam.nyekilajos.archcomppoc.inject

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaType

open class DaggerFragmentWithViewModel : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.javaClass.kotlin.declaredMemberProperties.filter {
            it.annotations.find { annotation -> annotation.annotationClass == InjectViewModel::class } != null
        }.forEach {
            if (it is KMutableProperty<*>) {
                @Suppress("UNCHECKED_CAST")
                val vm = ViewModelProviders.of(this, viewModelFactory).get(it.returnType.javaType as Class<out ViewModel>)
                it.setter.call(this, vm)
            }
        }
    }
}

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class InjectViewModel

