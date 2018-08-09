package com.epam.nyekilajos.archcomppoc.ui.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.epam.nyekilajos.archcomppoc.R
import com.epam.nyekilajos.archcomppoc.repository.AddressItem
import com.epam.nyekilajos.archcomppoc.repository.WidgetProperties
import com.epam.nyekilajos.archcomppoc.service.CallHandlerService
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class CallAddressAppWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var widgetProperties: WidgetProperties

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds?.forEach { appWidgetId ->
            widgetProperties.getWidgetSettings(appWidgetId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onSuccess = { preference ->
                                context?.let {
                                    setupRemoteViews(context, preference.addressItem)
                                }
                            }
                    )
        }
    }
}

fun setupRemoteViews(context: Context, addressItem: AddressItem) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val pendingIntent = PendingIntent.getService(
            context,
            0,
            Intent(context, CallHandlerService::class.java).apply {
                putExtra(CallHandlerService.EXTRA_IP_ADDRESS, addressItem.ipAddress)
                putExtra(CallHandlerService.EXTRA_PORT, addressItem.port)
            },
            PendingIntent.FLAG_UPDATE_CURRENT)

    val views = RemoteViews(context.packageName, R.layout.app_widget).apply {
        setOnClickPendingIntent(R.id.call_button, pendingIntent)
        setTextViewText(R.id.address_text, "${addressItem.ipAddress}:${addressItem.port}")
    }
    appWidgetManager.updateAppWidget(ComponentName(context, CallAddressAppWidgetProvider::class.java), views)
}
