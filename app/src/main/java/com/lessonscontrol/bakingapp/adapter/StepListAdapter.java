package com.lessonscontrol.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lessonscontrol.bakingapp.R;
import com.lessonscontrol.bakingapp.activity.StepDetailFragment;
import com.lessonscontrol.bakingapp.activity.StepListActivity;
import com.lessonscontrol.bakingapp.data.Recipe;
import com.lessonscontrol.bakingapp.data.Step;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepViewHolder> {

    private final StepListActivity parentActivity;

    private final Recipe recipe;

    private final boolean isTwoPaneModeBeingUsed;

    public StepListAdapter(StepListActivity parent,
                           Recipe recipe,
                           boolean isTwoPaneModeBeingUsed) {
        this.recipe = recipe;
        parentActivity = parent;
        this.isTwoPaneModeBeingUsed = isTwoPaneModeBeingUsed;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new StepViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final StepViewHolder holder, int position) {
        Step step = recipe.getSteps().get(position);
        int stepId = step.getId() + 1;
        holder.stepIdView.setText(Integer.toString(stepId));
        holder.stepDescriptionView.setText(step.getShortDescription());

        holder.itemView.setTag(step.getId());
        holder.itemView.setOnClickListener(onClickListener(position));
        if (isTwoPaneModeBeingUsed) {
            highlightSelectedItem(holder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        return recipe.getSteps().size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {
        final TextView stepIdView;
        final TextView stepDescriptionView;

        StepViewHolder(View view) {
            super(view);
            stepIdView = view.findViewById(R.id.item_id);
            stepDescriptionView = view.findViewById(R.id.item_short_description);
        }
    }

    private View.OnClickListener onClickListener(int position) {
        return view -> {
            if (isTwoPaneModeBeingUsed) {
                parentActivity.setSelectedItem(position);
            }
            Integer stepId = (Integer) view.getTag();
            StepDetailFragment fragment = new StepDetailFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable(Step.PARCELABLE_KEY, recipe.getSteps().get(stepId));
            arguments.putBoolean(Step.IS_LAST, recipe.getSteps().size() == (stepId + 1));
            fragment.setArguments(arguments);
            if (isTwoPaneModeBeingUsed) {
                parentActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                parentActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        };
    }

    private void highlightSelectedItem(View view, int position) {
        if (parentActivity.getSelectedItem() == position) {
            view.setBackgroundColor(parentActivity.getResources().getColor(R.color.colorLight));
        } else {
            //Paints unfocused items as white, otherwise recycled views will remain green
            view.setBackgroundColor(Color.WHITE);
        }
    }
}
