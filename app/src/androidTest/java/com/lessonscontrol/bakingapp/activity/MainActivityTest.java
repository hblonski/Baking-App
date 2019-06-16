package com.lessonscontrol.bakingapp.activity;

import android.content.res.AssetManager;
import android.widget.GridView;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.lessonscontrol.bakingapp.BuildConfig;
import com.lessonscontrol.bakingapp.R;
import com.lessonscontrol.bakingapp.adapter.RecipeListAdapter;
import com.lessonscontrol.bakingapp.data.Recipe;
import com.lessonscontrol.bakingapp.util.JSONHelper;
import com.lessonscontrol.bakingapp.util.ObjectUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void clean() {
        Intents.release();
    }

    @Test
    public void should_loadRecipeList_when_activityLoaded() {

        GridView recipesGridView = mainActivityTestRule
                .getActivity()
                .findViewById(R.id.recipe_grid);

        RecipeListAdapter recipeListAdapter = (RecipeListAdapter) recipesGridView.getAdapter();

        assertNotNull(recipeListAdapter);
        assertEquals(getRecipeListSize(), recipeListAdapter.getCount());
    }

    @Test
    public void should_loadStepListActivity_when_recipeClicked() {
        onData(anything())
                .inAdapterView(withId(R.id.recipe_grid))
                .atPosition(0)
                .perform(click());
        intended(hasComponent(StepListActivity.class.getName()));
    }

    private int getRecipeListSize() {

        AssetManager assets = mainActivityTestRule.getActivity().getAssets();

        List<Recipe> recipeList = ObjectUtils.nvl(JSONHelper.loadObjectListFromJSONAsset(assets,
                BuildConfig.RECIPES_FILE_NAME,
                Recipe.class), new ArrayList<>());

        return recipeList.size();
    }
}
