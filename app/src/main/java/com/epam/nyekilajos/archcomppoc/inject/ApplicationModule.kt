package com.epam.nyekilajos.archcomppoc.inject

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.epam.nyekilajos.archcomppoc.ArchCompApplication
import com.epam.nyekilajos.archcomppoc.MainActivity
import com.epam.nyekilajos.archcomppoc.network.CallHandler
import com.epam.nyekilajos.archcomppoc.network.CallHandlerImpl
import com.epam.nyekilajos.archcomppoc.repository.AddressDataBase
import com.epam.nyekilajos.archcomppoc.repository.AddressRepository
import com.epam.nyekilajos.archcomppoc.ui.adresslist.AddressListFragment
import com.epam.nyekilajos.archcomppoc.ui.createaddress.CreateAddressItemFragment
import com.epam.nyekilajos.archcomppoc.viewmodel.addresslist.AddressListViewModel
import com.epam.nyekilajos.archcomppoc.viewmodel.createaddress.CreateAddressViewModel
import dagger.*
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
abstract class ApplicationModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun providesContext(application: ArchCompApplication): Context = application.applicationContext

        @Singleton
        @JvmStatic
        @Provides
        fun providesAddressRepository(context: Context): AddressRepository {
            return Room.databaseBuilder(context, AddressDataBase::class.java, ADDRESS_ITEM_DATABASE_NAME).build()
        }
    }

    @Binds
    abstract fun bindsCallHandler(callHandlerImpl: CallHandlerImpl): CallHandler

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

    @ContributesAndroidInjector
    abstract fun contributeCreateAddressItemFragmentInjector(): CreateAddressItemFragment
}

@Module
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerAwareViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AddressListViewModel::class)
    abstract fun bindAddressListViewModel(viewModel: AddressListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAddressViewModel::class)
    abstract fun bindCreateAddressViewModel(viewModel: CreateAddressViewModel): ViewModel
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

private const val ADDRESS_ITEM_DATABASE_NAME = "addressItemDb"
