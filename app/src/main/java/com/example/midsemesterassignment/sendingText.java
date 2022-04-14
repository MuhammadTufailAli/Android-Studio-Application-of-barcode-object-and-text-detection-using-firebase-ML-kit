package com.example.midsemesterassignment;

public class sendingText {

    private String text=null;
    private static final  sendingText instance =new sendingText();
    public sendingText(){}

    public static sendingText getInstance(){return  instance;}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
