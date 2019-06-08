package com.lessonscontrol.bakingapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.lessonscontrol.bakingapp.R;
import com.lessonscontrol.bakingapp.data.Ingredient;
import com.lessonscontrol.bakingapp.data.Recipe;

/**
 * A fragment representing ingredients listing screen.
 */
public class IngredientsFragment extends Fragment {

    private Recipe recipe;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientsFragment() {
        //Empty
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null && arguments.containsKey(Recipe.PARCELABLE_KEY)) {
            recipe = arguments.getParcelable(Recipe.PARCELABLE_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.header_ingredients, container, false);

        if (recipe != null) {
            ((TextView) rootView.findViewById(R.id.label_recipe_name)).setText(recipe.getName());
            ((TextView) rootView.findViewById(R.id.ingredient_list))
                    .setText(Ingredient.formatIngredientList(recipe.getIngredients()));
        }

        return rootView;
    }
}
