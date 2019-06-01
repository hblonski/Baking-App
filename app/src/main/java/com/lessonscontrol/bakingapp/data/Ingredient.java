package com.lessonscontrol.bakingapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
class Ingredient {

    @JsonProperty("ingredient")
    private String name;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("measure")
    private String measure;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return this.measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
