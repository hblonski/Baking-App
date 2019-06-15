package com.lessonscontrol.bakingapp.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ListView;

import com.lessonscontrol.bakingapp.BuildConfig;
import com.lessonscontrol.bakingapp.R;
import com.lessonscontrol.bakingapp.adapter.RecipeListAdapter;
import com.lessonscontrol.bakingapp.data.Recipe;
import com.lessonscontrol.bakingapp.util.JSONHelper;
import com.lessonscontrol.bakingapp.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The configuration screen for the {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigureActivity extends Activity implements RecipeSelector {

    public static final String WIDGET_RECIPES_CONFIG = "widgetRecipesConfig";

    private static final int NO_RECIPE_FOUND = -1;

    private static List<Recipe> recipeList;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        if (recipeList == null) {
            loadRecipeList(getAssets());
        }

        setContentView(R.layout.ingredients_widget_configure);
        ListView recipeListView = findViewById(R.id.recipe_list);
        RecipeListAdapter recipeListAdapter = new RecipeListAdapter(recipeList, this);
        recipeListView.setAdapter(recipeListAdapter);
    }

    @Override
    public void selectRecipe(Integer recipeId) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        addWidgetRecipe(appWidgetId, recipeId);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        IngredientsWidget.updateAppWidget(this, appWidgetManager, appWidgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void addWidgetRecipe(int widgetId, int recipeId) {
        SharedPreferences.Editor editor = getSharedPreferences(WIDGET_RECIPES_CONFIG, MODE_PRIVATE).edit();
        editor.putInt(getWidgetCode(widgetId), recipeId);
        editor.apply();
    }

    public static Recipe getWidgetRecipe(int widgetId,
                                         SharedPreferences preferences,
                                         AssetManager assets) {
        int recipeId = preferences.getInt(getWidgetCode(widgetId), NO_RECIPE_FOUND);
        if (recipeList == null) {
            loadRecipeList(assets);
        }
        if (recipeId != NO_RECIPE_FOUND) {
            return recipeList
                    .stream()
                    .filter(r -> r.getId().equals(recipeId))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public static void removeWidgetRecipe(int widgetId,
                                          SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(getWidgetCode(widgetId));
        editor.apply();
    }

    private static void loadRecipeList(AssetManager assets) {
        recipeList = ObjectUtils.nvl(JSONHelper.loadObjectListFromJSONAsset(assets,
                BuildConfig.RECIPES_FILE_NAME,
                Recipe.class), new ArrayList<>());
    }

    //Widget code with prefix 'w'. Used to identify the widget in SharedPreferences
    private static String getWidgetCode(int widgetId) {
        return "w" + widgetId;
    }
}

