package com.example.dldke.foodbox.HalfRecipe;

public class DCItem {
    private Integer dueDate;
    private Double count;
    private String strDueDate;
    private String name;

    public DCItem(Integer dueDate, Double count) {
        this.dueDate = dueDate;
        this.count = count;
    }

    public DCItem(Integer dueDate, String name) {
        this.dueDate = dueDate;
        this.name = name;
    }

    public DCItem(String strDueDate, Double count) {
        this.strDueDate = strDueDate;
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

    public String getStrDueDate() {
        return strDueDate;
    }

    public void setStrDueDate(String strDueDate) {
        this.strDueDate = strDueDate;
    }

    public String getName() {
        return name;
    }
}
