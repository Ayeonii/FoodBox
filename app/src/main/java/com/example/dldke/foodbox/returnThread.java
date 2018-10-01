package com.example.dldke.foodbox;

public class returnThread extends Thread {

    private CustomRunnable target;
    public returnThread(CustomRunnable target) {
        super(target);
        this.target = target;
    }
    public Object getResult(){
        return target.getResult();
    }
}
