package com.example.midsemesterassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton barcodeImageButton;
    ImageButton contentImageButton;
    ImageButton textImageButton;
    Button ViewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Image Analysis using ML Kit");

        barcodeImageButton=(ImageButton) findViewById(R.id.imageButton);
        ViewList=findViewById(R.id.button);

        ViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity= new Intent(MainActivity.this,AllAnalysedImages.class);
                startActivity(intentLoadNewActivity);
            }
        });

        barcodeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity= new Intent(MainActivity.this,Barcode.class);
                startActivity(intentLoadNewActivity);
            }
        });

        contentImageButton=(ImageButton) findViewById(R.id.imageButton3);

        contentImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity= new Intent(MainActivity.this,Content.class);
                startActivity(intentLoadNewActivity);
            }
        });

        textImageButton=(ImageButton) findViewById(R.id.imageButton5);

        textImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity= new Intent(MainActivity.this,Text.class);
                startActivity(intentLoadNewActivity);
            }
        });
    }
}