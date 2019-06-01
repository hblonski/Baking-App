package com.lessonscontrol.bakingapp;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.lessonscontrol.bakingapp.adapter.RecipeListAdapter;
import com.lessonscontrol.bakingapp.data.Recipe;
import com.lessonscontrol.bakingapp.util.JSONHelper;
import com.lessonscontrol.bakingapp.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String RECIPES_FILE_NAME = "recipes.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Recipe> recipeList = ObjectUtils.nvl(JSONHelper.loadObjectListFromJSONAsset(getAssets(),
                RECIPES_FILE_NAME,
                Recipe.class), new ArrayList<Recipe>());

        RecipeListAdapter recipeListAdapter = new RecipeListAdapter(recipeList, this);
        GridView recipesGridView = findViewById(R.id.recipe_grid);
        recipesGridView.setAdapter(recipeListAdapter);
    }
}
