package com.example.midsemesterassignment;

public class sendingText2 {

    private String text=null;
    private static final  sendingText2 instance =new sendingText2();
    public sendingText2(){}

    public static sendingText2 getInstance(){return  instance;}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
