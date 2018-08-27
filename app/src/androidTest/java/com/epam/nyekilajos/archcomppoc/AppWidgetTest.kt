package com.epam.nyekilajos.archcomppoc

import android.content.Intent
import android.graphics.Point
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.test.InstrumentationRegistry
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppWidgetTest {

    private lateinit var uiDevice: UiDevice

    @Before
    fun setUp() {
        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun startMainActivityFromHomeScreen() {
        uiDevice.run {
            goToHome()
            openApp()
            clickFab()
            createAddressItem()
            goToHome()
            addNewWidget()
            placeAppWidgetToHomeScreen()
            configureAppWidget()
            removeAppWidget()
        }
    }

    private fun UiDevice.goToHome() {
        pressHome()

        wait(Until.hasObject(By.pkg(uiDevice.launcherPackageName).depth(0)), TIMEOUT)
    }

    private fun UiDevice.openApp() {
        InstrumentationRegistry.getContext().run {
            startActivity(
                    packageManager.getLaunchIntentForPackage(APP_PACKAGE_NAME)
                            ?.apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }
            )
        }
        wait(Until.hasObject(By.pkg(APP_PACKAGE_NAME).depth(0)), TIMEOUT)
    }

    private fun UiDevice.clickFab() {
        wait(Until.hasObject(By.res(APP_PACKAGE_NAME, "fab")), TIMEOUT)

        findObject(By.res(APP_PACKAGE_NAME, "fab")).click()
        wait(Until.hasObject(By.text("Address details")), TIMEOUT)
    }

    private fun UiDevice.createAddressItem() {
        findObject(By.res(APP_PACKAGE_NAME, "name_edit_text"))
                .text = TEST_ADDRESS_NAME
        findObject(By.res(APP_PACKAGE_NAME, "ip_address_edit_text"))
                .text = "www.google.com"
        findObject(By.res(APP_PACKAGE_NAME, "port_edit_text"))
                .text = "80"
        pressEnter()
        findObject(By.res(APP_PACKAGE_NAME, "create_address_button")).click()
        wait(Until.hasObject(By.text("Address List")), TIMEOUT)
    }

    private fun UiDevice.addNewWidget() {
        swipe(uiDevice.displayWidth / 2, uiDevice.displayHeight / 2, uiDevice.displayWidth / 2, uiDevice.displayHeight / 2, 400)
    }

    private fun UiDevice.placeAppWidgetToHomeScreen() {
        wait(Until.hasObject(By.clazz(TextView::class.java).text("Widgets")), TIMEOUT)

        findObject(By.clazz(TextView::class.java).text("Widgets")).click()
        wait(Until.hasObject(By.res(uiDevice.launcherPackageName, "widget_preview")), TIMEOUT)

        findObject(By.res(uiDevice.launcherPackageName, "widget_preview"))
                .drag(Point(uiDevice.displayWidth / 2, uiDevice.displayHeight / 2), DRAG_SPEED)
        wait(Until.hasObject(By.clazz(Spinner::class.java)), TIMEOUT)
    }

    private fun UiDevice.configureAppWidget() {
        findObject(By.clazz(Spinner::class.java))
                .click()
        wait(Until.hasObject(By.text(TEST_ADDRESS_NAME)), TIMEOUT)

        findObject(By.text(TEST_ADDRESS_NAME))
                .click()
        wait(Until.hasObject(By.clazz(Button::class.java).text("CREATE")), TIMEOUT)

        findObject(By.clazz(Button::class.java).text("CREATE"))
                .click()
        waitForIdle(TIMEOUT)
        wait(Until.hasObject(By.clazz(ImageView::class.java).descContains("Call")), TIMEOUT)

        assertTrue(hasObject(By.clazz(ImageView::class.java).descContains("Call")))
    }

    private fun UiDevice.removeAppWidget() {
        findObject(By.clazz(ImageView::class.java).descContains("Call"))
                .drag(Point(displayWidth / 2, 100), DRAG_SPEED)

        wait(Until.gone(By.clazz(ImageView::class.java).desc("Call")), TIMEOUT)

        assertFalse(hasObject(By.clazz(ImageView::class.java).desc("Call")))
    }
}

private const val TIMEOUT = 5000L
private const val TEST_ADDRESS_NAME = "Google"
private const val DRAG_SPEED = 400

private val APP_PACKAGE_NAME = MainActivity::class.java.`package`?.name!!
