package com.example.intentapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyShopNavigationTest {
    @Test
    fun bottomNavigationOpensAllThreeTabs() {
        ActivityScenario.launch(MainActivity::class.java).use {
            onView(withId(R.id.btn_buy)).check(matches(isDisplayed()))
            onView(withId(R.id.cartFragment)).perform(click())
            onView(withId(R.id.txt_cart)).check(matches(withText(R.string.title_cart)))
            onView(withId(R.id.profileFragment)).perform(click())
            onView(withId(R.id.txt_profile)).check(matches(withText(R.string.title_profile)))
            onView(withId(R.id.homeFragment)).perform(click())
            onView(withId(R.id.btn_buy)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun productNameIsPassedToCheckout() {
        ActivityScenario.launch(MainActivity::class.java).use {
            openCheckout()
            onView(withId(R.id.txt_product_name))
                .check(matches(withText(R.string.product_samsung_m22)))
            onView(withId(R.id.edt_address)).check(matches(withText("")))
        }
    }

    @Test
    fun everyProvinceCanBeReturnedToCheckout() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            var provinces = emptyArray<String>()
            scenario.onActivity { provinces = it.resources.getStringArray(R.array.provinces) }
            openCheckout()
            provinces.forEach { province ->
                chooseProvince(province)
                onView(withId(R.id.edt_address)).check(matches(withText(province)))
                onView(withId(R.id.txt_product_name))
                    .check(matches(withText(R.string.product_samsung_m22)))
            }
        }
    }

    @Test
    fun checkoutDoneReturnsHomeAndNewCheckoutHasNoOldAddress() {
        ActivityScenario.launch(MainActivity::class.java).use {
            openCheckout()
            chooseProvince("Jawa Tengah")
            onView(withId(R.id.btn_done)).perform(click())
            onView(withId(R.id.btn_buy)).check(matches(isDisplayed()))
            openCheckout()
            onView(withId(R.id.edt_address)).check(matches(withText("")))
        }
    }

    @Test
    fun backCancelsUnconfirmedProvinceChange() {
        ActivityScenario.launch(MainActivity::class.java).use {
            openCheckout()
            chooseProvince("Bali")
            onView(withId(R.id.edt_address)).perform(click())
            onView(withId(R.id.spinnerProvinces)).check(matches(withSpinnerText("Bali")))
            onView(withId(R.id.spinnerProvinces)).perform(click())
            onData(equalTo("Jawa Barat")).perform(click())
            pressBack()
            onView(withId(R.id.edt_address)).check(matches(withText("Bali")))
            pressBack()
            onView(withId(R.id.btn_buy)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun checkoutRetainsProductAndAddressAfterRecreation() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            openCheckout()
            chooseProvince("Jawa Timur")
            onView(withId(R.id.edt_address)).check(matches(withText("Jawa Timur")))
            scenario.recreate()
            onView(withId(R.id.txt_product_name))
                .check(matches(withText(R.string.product_samsung_m22)))
            onView(withId(R.id.edt_address)).check(matches(withText("Jawa Timur")))
        }
    }

    @Test
    fun addressSelectionSurvivesRecreationAndReturnsToCheckout() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            openCheckout()
            onView(withId(R.id.edt_address)).perform(click())
            onView(withId(R.id.spinnerProvinces)).perform(click())
            onData(equalTo("Jambi")).perform(click())
            scenario.recreate()
            onView(withId(R.id.spinnerProvinces)).check(matches(withSpinnerText("Jambi")))
            onView(withId(R.id.btn_done)).perform(click())
            onView(withId(R.id.edt_address)).check(matches(withText("Jambi")))
        }
    }

    @Test
    fun selectedTabSurvivesRecreation() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            onView(withId(R.id.profileFragment)).perform(click())
            onView(withId(R.id.txt_profile)).check(matches(isDisplayed()))
            scenario.recreate()
            onView(withId(R.id.txt_profile)).check(matches(isDisplayed()))
            onView(withId(R.id.homeFragment)).perform(click())
            onView(withId(R.id.btn_buy)).check(matches(isDisplayed()))
        }
    }

    private fun openCheckout() {
        onView(withId(R.id.btn_buy)).perform(click())
    }

    private fun chooseProvince(province: String) {
        onView(withId(R.id.edt_address)).perform(click())
        onView(withId(R.id.spinnerProvinces)).perform(click())
        onData(equalTo(province)).perform(click())
        onView(withId(R.id.btn_done)).perform(click())
    }
}
