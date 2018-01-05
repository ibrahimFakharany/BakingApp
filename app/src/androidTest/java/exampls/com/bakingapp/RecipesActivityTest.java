package exampls.com.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import exampls.com.bakingapp.R;
import exampls.com.bakingapp.UI.RecipesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void recipesActivityTest() {
        ViewInteraction imageView = onView(
                allOf(withId(R.id.image_view),
                        childAtPosition(
                                allOf(withId(R.id.card_view_recipe),
                                        childAtPosition(
                                                withId(R.id.recipes_rv),
                                                0)),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.recipe_name), withText("Brownies"),
                        childAtPosition(
                                allOf(withId(R.id.card_view_recipe),
                                        childAtPosition(
                                                withId(R.id.recipes_rv),
                                                1)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("Brownies")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.recipe_name), withText("Brownies"),
                        childAtPosition(
                                allOf(withId(R.id.card_view_recipe),
                                        childAtPosition(
                                                withId(R.id.recipes_rv),
                                                1)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Brownies")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.recipe_name), withText("Brownies"),
                        childAtPosition(
                                allOf(withId(R.id.card_view_recipe),
                                        childAtPosition(
                                                withId(R.id.recipes_rv),
                                                1)),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Brownies")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.recipe_name), withText("Brownies"),
                        childAtPosition(
                                allOf(withId(R.id.card_view_recipe),
                                        childAtPosition(
                                                withId(R.id.recipes_rv),
                                                1)),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Brownies")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipes_rv),
                        withParent(allOf(withId(R.id.frame_layout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.rv_steps), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.image_iv_previous_video), isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.image_iv_next_video), isDisplayed()));
        appCompatImageView2.perform(click());

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.image_iv_next_video), isDisplayed()));
        appCompatImageView3.perform(click());

        ViewInteraction appCompatImageView4 = onView(
                allOf(withId(R.id.image_iv_next_video), isDisplayed()));
        appCompatImageView4.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.tv), withText("3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_description),
                                        0),
                                1),
                        isDisplayed()));
        textView5.check(matches(withText("3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
