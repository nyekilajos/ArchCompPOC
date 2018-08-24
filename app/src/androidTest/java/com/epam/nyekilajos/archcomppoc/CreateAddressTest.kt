package com.epam.nyekilajos.archcomppoc


import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.epam.nyekilajos.archcomppoc.repository.Protocol
import com.epam.nyekilajos.archcomppoc.ui.adresslist.AddressViewHolder
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class CreateAddressTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testEmptyNameError() {
        onView(withId(R.id.adresslist))
                .check(matches(isDisplayed()))

        onView(withId(R.id.fab))
                .perform(click())

        onView(withId(R.id.address_details_title))
                .check(matches(isDisplayed()))

        onView(withId(R.id.name_edit_text))
                .perform(click())
                .perform(pressImeActionButton())

        onView(withId(R.id.ip_address_edit_text))
                .perform(pressImeActionButton())

        onView(withId(R.id.port_edit_text))
                .perform(pressImeActionButton())

        checkToast(R.string.empty_name_error, activityTestRule.activity)
    }

    @Test
    fun testAddressCreated() {
        onView(withId(R.id.adresslist))
                .check(matches(isDisplayed()))

        onView(withId(R.id.fab))
                .perform(click())

        onView(withId(R.id.address_details_title))
                .check(matches(isDisplayed()))

        onView(withId(R.id.name_edit_text))
                .perform(click())
                .perform(typeText("google"))
                .perform(pressImeActionButton())

        onView(withId(R.id.ip_address_edit_text))
                .perform(typeText("www.google.com"))
                .perform(pressImeActionButton())

        onView(withId(R.id.port_edit_text))
                .perform(typeText("80"))
                .perform(pressImeActionButton())

        Thread.sleep(3000)

        onView(withId(R.id.adresslist))
                .check(matches(isDisplayed()))

        onView(withId(R.id.address_list_recycler_view))
                .check(matches(hasChildCount(1)))
    }

    @Test
    fun testAddressRemoved() {
        onView(withId(R.id.adresslist))
                .check(matches(isDisplayed()))

        onView(withId(R.id.fab))
                .perform(click())

        onView(withId(R.id.address_details_title))
                .check(matches(isDisplayed()))

        onView(withId(R.id.name_edit_text))
                .perform(click())
                .perform(typeText("google"))
                .perform(pressImeActionButton())

        onView(withId(R.id.ip_address_edit_text))
                .perform(typeText("www.google.com"))
                .perform(pressImeActionButton())

        onView(withId(R.id.port_edit_text))
                .perform(typeText("80"))
                .perform(pressImeActionButton())

        Thread.sleep(3000)

        onView(withId(R.id.adresslist))
                .check(matches(isDisplayed()))

        onView(withId(R.id.fab))
                .perform(click())

        onView(withId(R.id.address_details_title))
                .check(matches(isDisplayed()))

        onView(withId(R.id.name_edit_text))
                .perform(click())
                .perform(typeText("google2"))
                .perform(pressImeActionButton())

        onView(withId(R.id.ip_address_edit_text))
                .perform(typeText("www.google.com"))
                .perform(pressImeActionButton())

        onView(withId(R.id.protocol_picker))
                .perform(click())

        onData(allOf(`is`(instanceOf(Protocol::class.java)), `is`(Protocol.HTTPS)))
                .perform(click())

        onView(withId(R.id.protocol_picker))
                .check(matches(withSpinnerText(Protocol.HTTPS.name)))

        onView(withId(R.id.port_edit_text))
                .perform(typeText("80"))
                .perform(pressImeActionButton())

        Thread.sleep(3000)

        onView(withId(R.id.adresslist))
                .check(matches(isDisplayed()))

        onView(withId(R.id.address_list_recycler_view))
                .check(matches(hasChildCount(2)))

        onView(withId(R.id.address_list_recycler_view))
                .perform(actionOnItemAtPosition<AddressViewHolder>(0, swipeLeft()))

        Thread.sleep(3000)

        onView(withId(R.id.address_list_recycler_view))
                .check(matches(hasChildCount(1)))
    }
}
