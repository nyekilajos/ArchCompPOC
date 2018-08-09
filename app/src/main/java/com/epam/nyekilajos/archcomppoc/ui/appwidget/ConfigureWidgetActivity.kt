package com.epam.nyekilajos.archcomppoc.ui.appwidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.epam.nyekilajos.archcomppoc.R
import com.epam.nyekilajos.archcomppoc.databinding.ConfigureWidgetActivityBinding
import com.epam.nyekilajos.archcomppoc.inject.DaggerActivityWithViewModel
import com.epam.nyekilajos.archcomppoc.inject.daggerViewModel
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.ui.common.BindableSpinnerAdapter
import com.epam.nyekilajos.archcomppoc.viewmodel.appwidget.AppWidgetConfigViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class ConfigureWidgetActivity : DaggerActivityWithViewModel() {

    private val viewModel: AppWidgetConfigViewModel by daggerViewModel(this)

    private lateinit var addressListAdapter: BindableSpinnerAdapter<AddressItem>

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setResult(RESULT_CANCELED)
        addressListAdapter = BindableSpinnerAdapter(this, R.layout.selected_dropdown_item, R.layout.dropdown_item)

        DataBindingUtil.setContentView<ConfigureWidgetActivityBinding>(this, R.layout.configure_widget_activity).apply {
            activity = this@ConfigureWidgetActivity
            adapter = addressListAdapter
            vm = viewModel
        }

        viewModel.addressItems.observe(this, Observer<List<AddressItem>> {
            addressListAdapter.setItems(it)
        })
    }

    fun saveAppWidget() {
        intent.extras
                ?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
                ?.let { appWidgetId ->
                    viewModel.saveWidgetSettings(appWidgetId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(
                                    onComplete = {
                                        viewModel.selectedAddressItem.value?.let {
                                            setupRemoteViews(this, it)
                                        }

                                        setResult(
                                                RESULT_OK,
                                                Intent().apply { putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId) }
                                        )
                                        finish()
                                    }
                            )
                }
    }
}
