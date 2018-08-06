package com.epam.nyekilajos.archcomppoc.inject

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.epam.nyekilajos.archcomppoc.ArchCompApplication
import com.epam.nyekilajos.archcomppoc.MainActivity
import com.epam.nyekilajos.archcomppoc.network.CallHandler
import com.epam.nyekilajos.archcomppoc.network.CallHandlerImpl
import com.epam.nyekilajos.archcomppoc.ui.adresslist.AddressListFragment
import com.epam.nyekilajos.archcomppoc.viewmodel.addresslist.AddressListViewModel
import dagger.*
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
class ApplicationModule {

    @Provides
    fun providesContext(application: ArchCompApplication): Context {
        return application.applicationContext
    }

    @Provides
    fun providesCallHandler(): CallHandler = CallHandlerImpl()
}

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivityInjector(): MainActivity
}

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeAddressListFragmentInjector(): AddressListFragment
}

@Module
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerAwareViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddressListViewModel::class)
    abstract fun bindAddressListViewModel(viewModel: AddressListViewModel): ViewModel
}

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, ActivityModule::class, FragmentModule::class, ViewModelBuilder::class])
interface AppComponent : AndroidInjector<ArchCompApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ArchCompApplication>()
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)