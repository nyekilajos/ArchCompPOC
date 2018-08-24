package com.epam.nyekilajos.archcomppoc

import android.app.Activity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers

fun checkToast(stringId: Int, activity: Activity) {
    Espresso.onView(ViewMatchers.withText(stringId)).inRoot(RootMatchers.withDecorView(Matchers.not(activity.getWindow().getDecorView()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}
