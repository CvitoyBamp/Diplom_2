package ru.yandex.model;

import java.util.ArrayList;

public class Ingredients {
    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    private ArrayList<String> ingredients;
}
