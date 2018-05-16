package com.wilbrom.zadtask;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SimpleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recyclerViewScroll_Check() {
        // if length of recyclerView = 9 it pass else it fails because only 10 items load at first start
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(6, scrollTo()));
    }
}
