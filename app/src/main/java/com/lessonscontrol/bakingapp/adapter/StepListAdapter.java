package com.lessonscontrol.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lessonscontrol.bakingapp.R;
import com.lessonscontrol.bakingapp.activity.StepDetailActivity;
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
                .inflate(R.layout.card_step, parent, false);
        return new StepViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final StepViewHolder holder, int position) {
        Step step = recipe.getSteps().get(position);
        Integer stepId = step.getId() + 1;
        holder.stepIdView.setText(stepId.toString());
        holder.stepDescriptionView.setText(step.getShortDescription());
//
//        holder.itemView.setTag(values.get(position));
//        holder.itemView.setOnClickListener(onClickListener);
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
            stepIdView = view.findViewById(R.id.step_id);
            stepDescriptionView = view.findViewById(R.id.step_short_description);
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Step step = (Step) view.getTag();
            if (isTwoPaneModeBeingUsed) {
                Bundle arguments = new Bundle();
                //arguments.putString(StepDetailFragment.ARG_ITEM_ID, item.id);
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);
                parentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, StepDetailActivity.class);
                //intent.putExtra(StepDetailFragment.ARG_ITEM_ID, item.id);
                context.startActivity(intent);
            }
        }
    };
}
