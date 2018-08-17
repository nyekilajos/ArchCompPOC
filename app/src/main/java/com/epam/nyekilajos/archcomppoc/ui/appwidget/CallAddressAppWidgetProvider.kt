package com.epam.nyekilajos.archcomppoc.ui.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
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
                            onSuccess = { addressItem ->
                                context?.let {
                                    setupRemoteViews(context, addressItem, appWidgetId)
                                }
                            },
                            onError = { Log.e(LOG_TAG, it.localizedMessage) }
                    )
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        appWidgetIds?.forEach { appWidgetId ->
            widgetProperties.delete(appWidgetId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onError = { Log.e(LOG_TAG, it.localizedMessage) })
        }
    }
}

fun setupRemoteViews(context: Context, addressItem: AddressItem, appWidgetId: Int) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val callPendingIntent = PendingIntent.getService(
            context,
            0,
            Intent(context, CallHandlerService::class.java).apply {
                putExtra(CallHandlerService.EXTRA_IP_ADDRESS, addressItem.ipAddress)
                putExtra(CallHandlerService.EXTRA_PORT, addressItem.port)
            },
            PendingIntent.FLAG_UPDATE_CURRENT)

    val settingsPendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, ConfigureWidgetActivity::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_CONFIGURE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            },
            PendingIntent.FLAG_UPDATE_CURRENT)

    val views = RemoteViews(context.packageName, R.layout.app_widget).apply {
        setOnClickPendingIntent(R.id.call_button, callPendingIntent)
        setOnClickPendingIntent(R.id.widget_settings, settingsPendingIntent)
        setTextViewText(R.id.address_name, addressItem.name)
        setTextViewText(R.id.address_text, "${addressItem.protocol.displayName}${addressItem.ipAddress}:${addressItem.port}")
    }
    appWidgetManager.updateAppWidget(ComponentName(context, CallAddressAppWidgetProvider::class.java), views)
}

private val LOG_TAG = CallAddressAppWidgetProvider::class.java.simpleName
