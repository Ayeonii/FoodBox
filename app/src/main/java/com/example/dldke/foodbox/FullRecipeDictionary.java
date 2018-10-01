package com.example.dldke.foodbox;

public class FullRecipeDictionary {

    private String Method;
    private String Minute;
    private String Fire;
    boolean selected;

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getMinute() {
        return Minute;
    }

    public void setMinute(String minute) {
        Minute = minute;
    }

    public String getFire() {
        return Fire;
    }

    public void setFire(String fire) {
        Fire = fire;
    }

    public FullRecipeDictionary(String method, String minute, String fire) {
        Method = method;
        Minute = minute;
        Fire = fire;
    }
}
