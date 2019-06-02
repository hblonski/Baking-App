package com.lessonscontrol.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lessonscontrol.bakingapp.R;
import com.lessonscontrol.bakingapp.activity.StepListActivity;
import com.lessonscontrol.bakingapp.data.Recipe;

import java.util.List;

public class RecipeListAdapter extends BaseAdapter {

    private final List<Recipe> recipeList;

    private final Context context;

    public RecipeListAdapter(@NonNull List<Recipe> recipeList, @NonNull Context context) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_item_image, parent, false);
        }

        Recipe recipe = recipeList.get(position);
        ((TextView) convertView.findViewById(R.id.item_description)).setText(recipe.getName());
        ((ImageView) convertView.findViewById(R.id.item_image)).setImageResource(R.drawable.ic_groceries);

        convertView.setOnClickListener(v -> {
            Intent stepListActivityIntent = new Intent(context, StepListActivity.class);
            stepListActivityIntent.putExtra(Recipe.PARCELABLE_KEY, recipe);
            context.startActivity(stepListActivityIntent);
        });

        return convertView;
    }
}
