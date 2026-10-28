package com.example.intentapp

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.button.MaterialButton
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun startsWithEightDisastersInTwoColumnGrid() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // Espresso menunggu layout selesai sebelum memeriksa ViewHolder yang tampil.
            onView(withId(R.id.btn_grid)).check(matches(isChecked()))
            scenario.onActivity { activity ->
                val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_disaster)
                assertEquals(8, recyclerView.adapter?.itemCount)
                assertEquals(2, (recyclerView.layoutManager as GridLayoutManager).spanCount)

                val firstItem = recyclerView.findViewHolderForAdapterPosition(0)!!.itemView
                assertEquals(
                    "Tsunami",
                    firstItem.findViewById<TextView>(R.id.txt_disaster_name).text.toString()
                )
                assertEquals(
                    "Natural",
                    firstItem.findViewById<TextView>(R.id.txt_disaster_type).text.toString()
                )
                assertNotNull(firstItem.findViewById<ImageView>(R.id.img_disaster).drawable)
            }
        }
    }

    @Test
    fun canSwitchLayoutsAndRestoreSelectionAfterRecreation() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            onView(withId(R.id.btn_list)).perform(click())
            assertListLayout(scenario)

            scenario.recreate()
            assertListLayout(scenario)

            onView(withId(R.id.btn_grid)).perform(click())
            scenario.recreate()
            scenario.onActivity { activity ->
                val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_disaster)
                assertEquals(2, (recyclerView.layoutManager as GridLayoutManager).spanCount)
                assertTrue(activity.findViewById<MaterialButton>(R.id.btn_grid).isChecked)
            }
        }
    }

    private fun assertListLayout(scenario: ActivityScenario<MainActivity>) {
        scenario.onActivity { activity ->
            val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_disaster)
            assertEquals(LinearLayoutManager::class.java, recyclerView.layoutManager!!::class.java)
            assertEquals(8, recyclerView.adapter?.itemCount)
            assertTrue(activity.findViewById<MaterialButton>(R.id.btn_list).isChecked)
        }
    }
}
