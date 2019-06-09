package com.lessonscontrol.bakingapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.lessonscontrol.bakingapp.R;
import com.lessonscontrol.bakingapp.adapter.StepListAdapter;
import com.lessonscontrol.bakingapp.data.Ingredient;
import com.lessonscontrol.bakingapp.data.Recipe;
import com.lessonscontrol.bakingapp.data.Step;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices.
 */
public class StepListActivity extends AppCompatActivity implements  RecipeStepNavigator {

    private static final int NO_ITEM_SELECTED = -1;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean isTwoPaneModeBeingUsed;

    private Recipe recipe;

    private int selectedItem = NO_ITEM_SELECTED;

    private StepListAdapter recyclerViewAdapter;

    private CardView ingredientsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        recipe = getIntent().getParcelableExtra(Recipe.PARCELABLE_KEY);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(recipe.getName());
        setSupportActionBar(toolbar);

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPaneModeBeingUsed = true;
        }

        View recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (isTwoPaneModeBeingUsed) {
            setupIngredientsCard();
        } else {
            setupHeader();
        }
    }

    private void setupHeader() {
        ((TextView) findViewById(R.id.label_recipe_name)).setText(recipe.getName());
        ((TextView) findViewById(R.id.ingredient_list))
                .setText(Ingredient.formatIngredientList(recipe.getIngredients()));
    }

    private void setupIngredientsCard() {
        ingredientsCard = findViewById(R.id.card_ingredients);
        ((ImageView) ingredientsCard.findViewById(R.id.item_image))
                .setImageResource(R.drawable.ic_groceries);
        ((TextView) ingredientsCard.findViewById(R.id.item_description))
                .setText(getResources().getString(R.string.label_ingredients));
        ingredientsCard.setOnClickListener(v -> {
            setSelectedItem(-1);
            v.setBackgroundColor(getResources().getColor(R.color.colorLight));
            Bundle arguments = new Bundle();
            arguments.putParcelable(Recipe.PARCELABLE_KEY, recipe);
            IngredientsFragment fragment = new IngredientsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerViewAdapter = new StepListAdapter(this, recipe, isTwoPaneModeBeingUsed);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void navigateBack(int currentStepId) {
        int nextStepId = currentStepId - 1;
        if (nextStepId >= 0) {
            navigate(recipe.getSteps().get(currentStepId - 1));
        }
    }

    @Override
    public void navigateForward(int currentStepId) {
        int nextStepId = currentStepId + 1;
        if (nextStepId < recipe.getSteps().size()) {
            navigate(recipe.getSteps().get(currentStepId + 1));
        }
    }

    private void navigate(Step nextStep) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        /*Clears the back stack in order to maintain the back button behavior of navigating to
         the step list activity.*/
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(Step.PARCELABLE_KEY, nextStep);
        arguments.putBoolean(Step.IS_LAST, recipe.getSteps().size() == (nextStep.getId() + 1));
        fragment.setArguments(arguments);
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        recyclerViewAdapter.notifyDataSetChanged();

        if (selectedItem != NO_ITEM_SELECTED) {
            ingredientsCard.setBackgroundColor(Color.WHITE);
        }
    }
}
