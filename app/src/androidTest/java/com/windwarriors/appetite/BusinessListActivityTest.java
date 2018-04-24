package com.windwarriors.appetite;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.KeyEvent;

import com.windwarriors.appetite.utils.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class BusinessListActivityTest {
    private Context context;

    @Rule
    public ActivityTestRule<BusinessListActivity> mActivityRule = new ActivityTestRule<>(
            BusinessListActivity.class);

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void loadMore() throws Exception {
        int nextPosition = Constants.PAGE_SIZE - 1;

        Thread.sleep(2000);
        onView(withId(R.id.recycler_view_business_list))
            .perform(RecyclerViewActions.scrollToPosition(nextPosition));

        Thread.sleep(800);
        onView(withId(R.id.recycler_view_business_list))
            .check(matches(hasDescendant(withText(R.string.next))));

        onView(allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
            withId(R.id.load_more_button)))
            .perform(click());

        onView(withId(R.id.recycler_view_business_list))
            .perform(RecyclerViewActions.scrollToPosition(10));

        Thread.sleep(800);
        onView(withId(R.id.recycler_view_business_list))
                .perform(RecyclerViewActions.scrollToPosition(nextPosition));

        onView(allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                withId(R.id.load_more_button)))
                .perform(click());

        Thread.sleep(2000);
    }

    @Test
    public void timHortonsSearch() throws InterruptedException {
        String timHortons = "Tim Hortons";
        int nextPosition = Constants.PAGE_SIZE - 1;

        onView(withId(R.id.action_filter))
            .perform(click());

        onView(withId(R.id.PriceLevel_1))
            .perform(click(), click());
        onView(withId(R.id.PriceLevel_4))
                .perform(click(), click());

        onView(withText("ok")).inRoot(isDialog())
            .check(matches(isDisplayed())).perform(click());


        onView(withId(R.id.menu_search))
            .perform(typeText(timHortons), pressKey(KeyEvent.KEYCODE_ENTER));
            //.perform(pressImeActionButton());
        closeSoftKeyboard();
        Thread.sleep(1000);

        onView(withId(R.id.recycler_view_business_list))
            .check(matches(hasDescendant(withText(timHortons))))
            .perform(RecyclerViewActions.scrollToPosition(nextPosition));

        Thread.sleep(1000);
        //go to details activity


        onView(withId(R.id.recycler_view_business_list))
            .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(1500);
        onView(withId(R.id.details_business_name))
            .check(matches(withText(timHortons)));

        //onView(withId(R.id.detailsScrollView))
        //    .perform(swipeDown(), swipeUp());

        onView(withId(R.id.details_image))
            .perform(swipeUp());

        onView(withId(R.id.photos_viewpager))
            .perform(swipeLeft(), swipeLeft(), swipeRight(), swipeRight(), swipeDown(),
            pressBack());

        Thread.sleep(2000);
        /*
        onData(instanceOf(BusinessAdapter.class))
            .inAdapterView(withId(R.id.recycler_view_business_list))
            .atPosition(nextPosition)
            .onChildView(withId(R.id.load_more_button))
            .perform(click());
            //.check(matches(withText(R.string.next)));
        */
    }
}