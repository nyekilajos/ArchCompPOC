package com.epam.nyekilajos.archcomppoc.inject

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.epam.nyekilajos.archcomppoc.ArchCompApplication
import com.epam.nyekilajos.archcomppoc.MainActivity
import com.epam.nyekilajos.archcomppoc.repository.*
import com.epam.nyekilajos.archcomppoc.service.CallHandler
import com.epam.nyekilajos.archcomppoc.service.CallHandlerService
import com.epam.nyekilajos.archcomppoc.service.CallHandlerWithHttpUrlConnection
import com.epam.nyekilajos.archcomppoc.ui.adresslist.AddressListFragment
import com.epam.nyekilajos.archcomppoc.ui.appwidget.CallAddressAppWidgetProvider
import com.epam.nyekilajos.archcomppoc.ui.appwidget.ConfigureWidgetActivity
import com.epam.nyekilajos.archcomppoc.ui.createaddress.CreateAddressItemFragment
import com.epam.nyekilajos.archcomppoc.viewmodel.addresslist.AddressListViewModel
import com.epam.nyekilajos.archcomppoc.viewmodel.appwidget.AppWidgetConfigViewModel
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
        fun providesAddressDataBase(context: Context): AddressDataBase {
            return Room.databaseBuilder(context, AddressDataBase::class.java, ADDRESS_ITEM_DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
        }
    }

    @Singleton
    @Binds
    abstract fun bindsWidgetProperties(addressDataBase: AddressDataBase): AddressRepository

    @Singleton
    @Binds
    abstract fun bindsAddressRepository(addressDataBase: AddressDataBase): WidgetProperties

    @Binds
    abstract fun bindsCallHandler(callHandlerWithHttpUrlConnection: CallHandlerWithHttpUrlConnection): CallHandler
}

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeConfigureWidgetActivityInjector(): ConfigureWidgetActivity
}

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeAddressListFragmentInjector(): AddressListFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateAddressItemFragmentInjector(): CreateAddressItemFragment
}

@Module
abstract class BroadcastReceiverModule {

    @ContributesAndroidInjector
    abstract fun contributeCallAddressAppWidgetProviderInjector(): CallAddressAppWidgetProvider
}

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun contributeCallHandlerServiceInjector(): CallHandlerService
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

    @Binds
    @IntoMap
    @ViewModelKey(AppWidgetConfigViewModel::class)
    abstract fun bindAppWidgetConfigViewModel(viewModel: AppWidgetConfigViewModel): ViewModel
}

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ActivityModule::class,
    FragmentModule::class,
    BroadcastReceiverModule::class,
    ServiceModule::class,
    ViewModelBuilder::class
])
interface AppComponent : AndroidInjector<ArchCompApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ArchCompApplication>()
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

private const val ADDRESS_ITEM_DATABASE_NAME = "addressItemDb"
