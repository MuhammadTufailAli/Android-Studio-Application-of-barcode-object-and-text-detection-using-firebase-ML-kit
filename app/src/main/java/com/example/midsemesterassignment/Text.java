package com.example.midsemesterassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

//import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

public class Text extends AppCompatActivity {
    ImageView imageView;
    Button btOpen;
    Button editresult;
    Button btgallary;

    Uri imageUri;

    TextView textview;
    Bitmap imageBitmap;
    public static String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        getSupportActionBar().setTitle("Text Reader");

        imageView =findViewById(R.id.image_view);
        btOpen=findViewById(R.id.bt_open);
        btgallary=findViewById(R.id.button2);

        textview=findViewById(R.id.textView5);
        editresult=findViewById(R.id.button4);




        btgallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 200);
            }
        });

        if(ContextCompat.checkSelfPermission( Text.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Text.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100 );
        }
        btOpen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                textview.setText("");
                Intent intent=new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
                startActivityForResult(intent, 100);

            }
        });




        editresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message=textview.getText().toString();
                Intent intent=new Intent(Text.this,EditTextContent.class);
                BitmapHelper.getInstance().setBitmap(imageBitmap);
                intent.putExtra("Extra message",message);
                sendingText2.getInstance().setText(message);


                startActivity(intent);
            }
        });
    }


    private Uri saveImage(Bitmap image, Context context) {

        File imageFolder=new File(context.getCacheDir(),"images");
        Uri uri=null;
        try{
            imageFolder.mkdir();
            File file=new File(imageFolder,"captured_image.jpg");
            FileOutputStream stream=new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(context.getApplicationContext(),"com.example.midsemesterassignment"+".provider",file);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return uri;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==100){
            imageBitmap=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(imageBitmap);


            WeakReference<Bitmap> result1= new WeakReference<>(Bitmap.createScaledBitmap(imageBitmap,
                    imageBitmap.getHeight(),imageBitmap.getWidth(),false).copy(
                    Bitmap.Config.RGB_565,true)
            );
            Bitmap bm=result1.get();
            imageUri=saveImage(bm,Text.this);

            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), imageUri);

                FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                        .getOnDeviceTextRecognizer();

                Task<FirebaseVisionText> result =
                        detector.processImage(image)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                        // Task completed successfully
                                        // ...
                                        textview.setText(firebaseVisionText.getText());
                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                                Toast.makeText(Text.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                Log.d("Error: ",e.getMessage());
                                            }
                                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (resultCode == RESULT_OK && requestCode==200) {

            try {
                InputStream inputStream=getContentResolver().openInputStream(data.getData());

                imageBitmap=BitmapFactory.decodeStream(inputStream);

                imageView.setImageBitmap(imageBitmap);

                WeakReference<Bitmap> result1= new WeakReference<>(Bitmap.createScaledBitmap(imageBitmap,
                        imageBitmap.getHeight(),imageBitmap.getWidth(),false).copy(
                        Bitmap.Config.RGB_565,true)
                );
                Bitmap bm=result1.get();
                imageUri=saveImage(bm,Text.this);

                FirebaseVisionImage image;
                try {
                    image = FirebaseVisionImage.fromFilePath(getApplicationContext(), imageUri);

                    FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                            .getOnDeviceTextRecognizer();

                    Task<FirebaseVisionText> result =
                            detector.processImage(image)
                                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                        @Override
                                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                            // Task completed successfully
                                            // ...
                                            textview.setText(firebaseVisionText.getText());
                                        }
                                    })
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Task failed with an exception
                                                    // ...
                                                    Toast.makeText(Text.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    Log.d("Error: ",e.getMessage());
                                                }
                                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
//
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Text.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
    }
//    private void detectTextFromImage() {
//
//        FirebaseVisionImage image;
//        try {
//            image = FirebaseVisionImage.fromFilePath(getApplicationContext(), imageUri);
//
//            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
//                    .getOnDeviceTextRecognizer();
//
//            Task<FirebaseVisionText> result =
//                    detector.processImage(image)
//                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//                                @Override
//                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
//                                    // Task completed successfully
//                                    // ...
//                                    textview.setText(firebaseVisionText.getText());
//                                }
//                            })
//                            .addOnFailureListener(
//                                    new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            // Task failed with an exception
//                                            // ...
//                                            Toast.makeText(Text.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            Log.d("Error: ",e.getMessage());
//                                        }
//                                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//





//FirebaseVisionImage firebaseVisionImage=FirebaseVisionImage.fromBitmap(captureImage);
//        FirebaseVisionTextDetector firebaseVisionTextDetector=FirebaseVision.getInstance().getVisionTextDetector();
//        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//            @Override
//            public void onSuccess(FirebaseVisionText firebaseVisionText) {
//                displayTextFromImage(firebaseVisionText);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Text.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("Error: ",e.getMessage());
//            }
//        });
//    }

//    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
//        List<FirebaseVisionText.Block> blockList=firebaseVisionText.getBlocks();
//        if(blockList.size()==0){
//            Toast.makeText(this,"No Text Found in image",Toast.LENGTH_SHORT).show();
//        }
//        else{
//            for(FirebaseVisionText.Block block : firebaseVisionText.getBlocks())
//            {
//                String text=block.getText();
//                textview.setText(text);
//            }
//        }
//    }
}