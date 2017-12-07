package exampls.com.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import exampls.com.bakingapp.UI.RecipesActivity;

import static android.support.test.espresso.action.ViewActions.click;

/**
 * Created by 450 G1 on 27/11/2017.
 */
@RunWith(JUnit4.class)
public class RecipesActivityTest {
    @Rule
    ActivityTestRule<RecipesActivity> recipesActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void recyclerViewTest(){

        Espresso.onView(ViewMatchers.withId(R.id.recipes_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Espresso.onView(ViewMatchers.withId(R.id.app_bar))
                .check(ViewAssertions.matches(ViewMatchers.withText("Nutella Pie")));

    }
}
