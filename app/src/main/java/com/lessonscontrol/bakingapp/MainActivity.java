package com.lessonscontrol.bakingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lessonscontrol.bakingapp.data.Recipe;
import com.lessonscontrol.bakingapp.util.JSONHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String RECIPES_FILE_NAME = "recipes.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Recipe> recipeList = JSONHelper.loadObjectListFromJSONAsset(getAssets(),
                RECIPES_FILE_NAME,
                Recipe.class);
    }
}
