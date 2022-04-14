package com.example.midsemesterassignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import androidx.annotation.Nullable;


import android.provider.MediaStore;
import android.util.Log;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class Barcode extends AppCompatActivity {

    ImageView imageView;

    Button btnScanBarcode, editresult;
    Bitmap photo;
    TextView textview;
    BarcodeScanner scanner;
    ActivityResultLauncher<Intent> cameraLauncher;
    ActivityResultLauncher<Intent> galleryLauncher;

    InputImage inputImage;

    private static final int CAMERA_PERMISSION_CODE=223;
    private static final int READ_STORAGE_PERMISSION_CODE=144;
    private static final int WRITE_STORAGE_PERMISSION_CODE=144;
    private static final String TAG="MyTag";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        getSupportActionBar().setTitle("Barcode Reader");

        //take photo
        imageView =findViewById(R.id.image_view);


        btnScanBarcode=findViewById(R.id.button3);
        scanner = BarcodeScanning.getClient();
        textview=findViewById(R.id.textView5);
        editresult=findViewById(R.id.button4);

        cameraLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data=result.getData();
                        try{
                            photo=(Bitmap) data.getExtras().get("Data");

                            inputImage=InputImage.fromBitmap(photo,0);

                            inputImage=InputImage.fromFilePath(Barcode.this,data.getData());
                            processImageQr();
                        }catch(Exception e){
                            Log.d(TAG,"onActivityResult: "+e.getMessage());
                        }

                    }
                }
        );

        galleryLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data=result.getData();
                        try{

                            inputImage=InputImage.fromFilePath(Barcode.this,data.getData());


                            processImageQr();
                        }catch(Exception e){
                                Log.d(TAG,"onActivityResult: "+e.getMessage());
                        }

                    }
                }
        );

        editresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message=textview.getText().toString();
                Intent intent=new Intent(Barcode.this,EditImageContent.class);
//                sendingText.getInstance().setText(message);
                BitmapHelper.getInstance().setBitmap(photo);
                intent.putExtra("Extra message",message);

                startActivity(intent);
            }
        });


        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String[] options={"Camera","Gallery"};

                AlertDialog.Builder builder=new AlertDialog.Builder(Barcode.this);
                builder.setTitle("Pick a option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if(which==0){
                                Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                cameraLauncher.launch(cameraIntent);
                        }else{
                            Intent storageIntent= new Intent();
                            storageIntent.setType("image/*");
                            storageIntent.setAction(Intent.ACTION_GET_CONTENT);
                            galleryLauncher.launch(storageIntent);

                        }
                    }
                });

                builder.show();

            }
        });

    }



    private void processImageQr() {
photo=inputImage.getBitmapInternal();
imageView.setImageBitmap(photo);


        Task<List<com.google.mlkit.vision.barcode.common.Barcode>> result = scanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<com.google.mlkit.vision.barcode.common.Barcode>>() {
            @Override
            public void onSuccess(List<com.google.mlkit.vision.barcode.common.Barcode> barcodes) {
                for(com.google.mlkit.vision.barcode.common.Barcode barcode: barcodes){
                    int valueType = barcode.getValueType();
                    // See API reference for complete list of supported types

                    switch (valueType) {
                        case com.google.mlkit.vision.barcode.common.Barcode.TYPE_WIFI:
                            String ssid = barcode.getWifi().getSsid();
                            String password = barcode.getWifi().getPassword();
                            int type = barcode.getWifi().getEncryptionType();
                            imageView.setImageBitmap(photo);
                            textview.setText("SSID: "+ssid+"\n"+
                                    "Password: "+password+"\n"+
                                    "Type: "+type+"\n");
                            break;
                        case com.google.mlkit.vision.barcode.common.Barcode.TYPE_URL:
                            String title = barcode.getUrl().getTitle();
                            String url = barcode.getUrl().getUrl();
                            imageView.setImageBitmap(photo);
                            textview.setText("Title: "+title+"\n"+
                                    "Url: "+url+"\n");
                            break;
                        default:
                            String data=barcode.getDisplayValue();
                            imageView.setImageBitmap(photo);
                            textview.setText("Result: "+data);
                            break;
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"OnFailure: "+e.getMessage());

            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        CheckPermission(Manifest.permission.CAMERA,CAMERA_PERMISSION_CODE);
    }

    public void CheckPermission(String permission, int reqestCode){
    if(ContextCompat.checkSelfPermission(Barcode.this,permission)== PackageManager.PERMISSION_DENIED){

        ActivityCompat.requestPermissions(Barcode.this, new String[]{permission},reqestCode);
    }

    }
@Override
public void onRequestPermissionsResult(int requestCode,@NonNull String[] permission, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permission,grantResults);

        if(requestCode==CAMERA_PERMISSION_CODE){
            if(!(grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(Barcode.this,"Camera permission Denied", Toast.LENGTH_SHORT).show();
            }else{
                CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_STORAGE_PERMISSION_CODE);
            }
        }else if(requestCode==READ_STORAGE_PERMISSION_CODE){
                if(!(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                    Toast.makeText(Barcode.this,"Storage permission Denied",Toast.LENGTH_SHORT).show();
                }else{
                    CheckPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_STORAGE_PERMISSION_CODE);
                }

            }else if(requestCode==WRITE_STORAGE_PERMISSION_CODE){
            if(!(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(Barcode.this,"Storage permission Denied",Toast.LENGTH_SHORT).show();
            }

        }
        }











        }