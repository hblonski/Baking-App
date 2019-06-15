package com.lessonscontrol.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.lessonscontrol.bakingapp.R;
import com.lessonscontrol.bakingapp.data.Ingredient;
import com.lessonscontrol.bakingapp.data.Recipe;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidgetConfigureActivity IngredientsWidgetConfigureActivity}
 */
public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        Recipe recipe = IngredientsWidgetConfigureActivity.getWidgetRecipe(appWidgetId,
                context.getSharedPreferences(
                        IngredientsWidgetConfigureActivity.WIDGET_RECIPES_CONFIG,
                        MODE_PRIVATE),
                context.getAssets());

        if (recipe != null) {
            views.setTextViewText(R.id.recipe_name, recipe.getName());
            views.setTextViewText(R.id.ingredient_list, Ingredient.formatIngredientList(recipe.getIngredients()));
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int widget : appWidgetIds) {
            IngredientsWidgetConfigureActivity.removeWidgetRecipe(widget,
                    context.getSharedPreferences(
                    IngredientsWidgetConfigureActivity.WIDGET_RECIPES_CONFIG,
                    MODE_PRIVATE));
        }
        super.onDeleted(context, appWidgetIds);
    }
}

