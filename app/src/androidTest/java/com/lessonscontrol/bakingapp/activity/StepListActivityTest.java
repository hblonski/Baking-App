package com.lessonscontrol.bakingapp.activity;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.lessonscontrol.bakingapp.R;
import com.lessonscontrol.bakingapp.adapter.StepListAdapter;
import com.lessonscontrol.bakingapp.data.Ingredient;
import com.lessonscontrol.bakingapp.data.Recipe;
import com.lessonscontrol.bakingapp.data.Step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class StepListActivityTest {

    private Recipe recipe = mockRecipe();

    @Rule
    public ActivityTestRule<StepListActivity> stepListActivityTestRule =
            new ActivityTestRule<StepListActivity>(StepListActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry
                            .getInstrumentation()
                            .getTargetContext();
                    Intent stepListActivityIntent = new Intent(targetContext, StepListActivity.class);
                    stepListActivityIntent.putExtra(Recipe.PARCELABLE_KEY, recipe);
                    return stepListActivityIntent;
                }
            };

    @Test
    public void should_loadRecipeStepList_when_activityLoaded() {

        RecyclerView stepsRecyclerView = stepListActivityTestRule
                .getActivity()
                .findViewById(R.id.step_list);

        StepListAdapter stepListAdapter = (StepListAdapter) stepsRecyclerView.getAdapter();

        assertNotNull(stepListAdapter);
        assertEquals(recipe.getSteps().size(), stepListAdapter.getItemCount());
    }

    @Test
    public void should_loadStepDetailsFragment_when_stepClicked() {
        onView(withId(R.id.step_list))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.fragment_step_detail)).check(matches(isDisplayed()));
    }

    @Test
    public void should_loadIngredientsCard_when_twoPaneModeBeingUsed() {
        if (isTwoPaneModeBeingUsed()) {
            onView(withId(R.id.card_ingredients)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void should_loadIngredients_when_ingredientsCardClicked() {
        if (isTwoPaneModeBeingUsed()) {
            onView(withId(R.id.card_ingredients)).check(matches(isDisplayed()));
            onView(withId(R.id.card_ingredients)).perform(click());
            onView(withId(R.id.header_ingredients)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void should_loadHeader_when_twoPaneModeNotBeingUsed() {
        if (!isTwoPaneModeBeingUsed()) {
            onView(withId(R.id.header_ingredients)).check(matches(isDisplayed()));
        }
    }

    /*
    * Navigation buttons test
    * */

    @Test
    public void should_navigateToNextStep_when_navigationForwardButtonClicked() {
        if (!isTwoPaneModeBeingUsed()) {
            onView(withId(R.id.step_list))
                    .perform(actionOnItemAtPosition(0, click()));
            onView(withId(R.id.fragment_step_detail)).check(matches(isDisplayed()));
            onView(withId(R.id.step_title)).check(matches(withText("step 1")));
            onView(withId(R.id.button_next_step)).check(matches(isDisplayed()));
            onView(withId(R.id.button_next_step)).perform(click());
            onView(withId(R.id.step_title)).check(matches(withText("step 2")));
        }
    }

    @Test
    public void should_navigateToPreviousStep_when_navigationBackButtonClicked() {
        if (!isTwoPaneModeBeingUsed()) {
            onView(withId(R.id.step_list))
                    .perform(actionOnItemAtPosition(1, click()));
            onView(withId(R.id.fragment_step_detail)).check(matches(isDisplayed()));
            onView(withId(R.id.step_title)).check(matches(withText("step 2")));
            onView(withId(R.id.button_previous_step)).check(matches(isDisplayed()));
            onView(withId(R.id.button_previous_step)).perform(click());
            onView(withId(R.id.step_title)).check(matches(withText("step 1")));
        }
    }

    @Test
    public void should_hideNavigationForwardButton_when_lastStepDisplayed() {
        if (!isTwoPaneModeBeingUsed()) {
            onView(withId(R.id.step_list))
                    .perform(actionOnItemAtPosition(0, click()));
            onView(withId(R.id.fragment_step_detail)).check(matches(isDisplayed()));
            onView(withId(R.id.step_title)).check(matches(withText("step 1")));
            onView(withId(R.id.button_next_step)).check(matches(isDisplayed()));
            onView(withId(R.id.button_next_step)).perform(click());
            onView(withId(R.id.step_title)).check(matches(withText("step 2")));
            onView(withId(R.id.button_next_step)).check(matches(not(isDisplayed())));
        }
    }

    @Test
    public void should_hideNavigationBackButton_when_firstStepDisplayed() {
        if (!isTwoPaneModeBeingUsed()) {
            onView(withId(R.id.step_list))
                    .perform(actionOnItemAtPosition(1, click()));
            onView(withId(R.id.fragment_step_detail)).check(matches(isDisplayed()));
            onView(withId(R.id.step_title)).check(matches(withText("step 2")));
            onView(withId(R.id.button_previous_step)).check(matches(isDisplayed()));
            onView(withId(R.id.button_previous_step)).perform(click());
            onView(withId(R.id.step_title)).check(matches(withText("step 1")));
            onView(withId(R.id.button_previous_step)).check(matches(not(isDisplayed())));
        }
    }

    private boolean isTwoPaneModeBeingUsed() {
        return stepListActivityTestRule
                .getActivity()
                .findViewById(R.id.step_detail_container) != null;
    }

    private Recipe mockRecipe() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setMeasure("cup");
        ingredient1.setName("ingredient test 1");
        ingredient1.setQuantity(1);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setMeasure("spoon");
        ingredient2.setName("ingredient test 2");
        ingredient2.setQuantity(2);
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        List<Step> steps = new ArrayList<>();
        Step step1 = new Step();
        step1.setDescription("step 1");
        step1.setId(0);
        step1.setShortDescription("step 1");
        Step step2 = new Step();
        step2.setDescription("step 1");
        step2.setId(1);
        step2.setShortDescription("step 2");
        steps.add(step1);
        steps.add(step2);

        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setName("test recipe");
        recipe.setServings(2);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);
        return recipe;
    }
}
