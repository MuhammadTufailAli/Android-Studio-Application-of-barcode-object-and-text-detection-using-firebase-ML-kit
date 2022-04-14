package com.example.midsemesterassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;

public class EditImageContent extends AppCompatActivity {
    Button save;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri imageUri;
    TextView imagetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image_content);
        getSupportActionBar().setTitle("Edit Barcode Image Content");

        Intent intent =getIntent();

        String message=intent.getStringExtra("Extra message");


        imagetext=findViewById(R.id.textView8);
        EditText editText2=findViewById(R.id.editTextTextPersonName);
        EditText editText=findViewById(R.id.editTextTextPersonName2);
        ImageView imageView=findViewById(R.id.imageView);
        save=findViewById(R.id.button5);

        Bitmap imageBitmap=BitmapHelper.getInstance().getBitmap();

        imagetext.setText(message);
        imageView.setImageBitmap(BitmapHelper.getInstance().getBitmap());
        editText.setText(imagetext.getText().toString());

        WeakReference<Bitmap> result= new WeakReference<>(Bitmap.createScaledBitmap(imageBitmap,
                imageBitmap.getHeight(),imageBitmap.getWidth(),false).copy(
                Bitmap.Config.RGB_565,true)
        );

        Bitmap bm=result.get();
        imageUri=saveImage(bm,EditImageContent.this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                storage=FirebaseStorage.getInstance();
                storageReference=storage.getReference();
                uploadPicture();


                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("analysed_images");
                String text1=imagetext.getText().toString();
                String text2=editText2.getText().toString();
                UserHelperClass helperClass=new UserHelperClass(imageUri.toString(),text2,text1);


                reference.push().setValue(helperClass);
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

    private void uploadPicture() {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();
        final String randomKey= UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/"+ randomKey);
        riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content),"Image Uploaded",Snackbar.LENGTH_LONG).show();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                pd.dismiss();
                // Handle unsuccessful uploads
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });
    }
}