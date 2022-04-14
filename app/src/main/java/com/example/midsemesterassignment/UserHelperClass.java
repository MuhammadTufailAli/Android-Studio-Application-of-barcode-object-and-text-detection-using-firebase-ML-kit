package com.example.midsemesterassignment;

import android.graphics.Bitmap;
import android.net.Uri;

public class UserHelperClass {
    String filname,reader,result;



    public UserHelperClass() {

    }

    public UserHelperClass(String filname,String reader, String result) {
        this.filname=filname;
        this.reader = reader;
        this.result = result;


    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFilname() {
        return filname;
    }

    public void setFilname(String filname) {
        this.filname = filname;
    }
}
