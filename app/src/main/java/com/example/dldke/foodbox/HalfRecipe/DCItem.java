package com.example.dldke.foodbox.HalfRecipe;

public class DCItem {
    private Integer dueDate;
    private Double count;

    public DCItem(Integer dueDate, Double count) {
        this.dueDate = dueDate;
        this.count = count;
    }

    public Integer getDueDate() {
        return dueDate;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
