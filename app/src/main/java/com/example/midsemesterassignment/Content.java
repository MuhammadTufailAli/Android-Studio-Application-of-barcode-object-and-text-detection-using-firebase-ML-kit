package com.example.midsemesterassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
//import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Content extends AppCompatActivity {
    ImageView imageView;
    Button btOpen;
    Button btgallary,editresult;
    Bitmap captureImage;
//    Button chose;
    TextView resultTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        getSupportActionBar().setTitle("Image Content Reader");

        imageView =findViewById(R.id.image_view1);
        btOpen=findViewById(R.id.bt_open);
        btgallary=findViewById(R.id.button2);
        resultTv=findViewById(R.id.textView5);
//        chose=findViewById(R.id.button3);
        editresult=findViewById(R.id.button4);

        btgallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 200);
            }
        });

        if(ContextCompat.checkSelfPermission( Content.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Content.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100 );
        }
        btOpen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                startActivityForResult(intent, 100);

            }
        });

//        chose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i= new Intent();
//                i.setType("image/*");
//                i.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(i,"Select images"),121);
//            }
//        });

        editresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(Content.this,EditContent.class);
                BitmapHelper.getInstance().setBitmap(captureImage);


                sendingText.getInstance().setText("Firebase dependency error");


                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
//        if(requestCode==121){
//            imageView.setImageURI(data.getData());
//
//
//            FirebaseVisionImage image;

            //Not working due to duplicate class error and there is no solution of it
//            try {


//                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), data.getData());
//                FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
//                        .getOnDeviceImageLabeler();
//
//                labeler.processImage(image)
//                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
//                            @Override
//                            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
//                                for (FirebaseVisionImageLabel label: labels) {
//                                    String text = label.getText();
//                                    String entityId = label.getEntityId();
//                                    float confidence = label.getConfidence();
//                                    resultTv.append(text+"    "+confidence+"\n");
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Task failed with an exception
//                                // ...
//                            }
//                        });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        if(requestCode==100){
            captureImage=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(captureImage);
        }

        if (resultCode == RESULT_OK && requestCode==200) {
            try {
                InputStream inputStream=getContentResolver().openInputStream(data.getData());

                captureImage=BitmapFactory.decodeStream(inputStream);

                imageView.setImageBitmap(captureImage);
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Content.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
    }
}