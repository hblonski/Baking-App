package com.lessonscontrol.bakingapp.activity;

public interface RecipeStepNavigator {

    void navigateBack(int currentStepId);

    void navigateForward(int currentStepId);
}
