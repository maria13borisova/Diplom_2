package ru.pojo.Order;

import java.util.ArrayList;

public class OrderRequest {
    private ArrayList<String> ingredients = new ArrayList<>();


    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
