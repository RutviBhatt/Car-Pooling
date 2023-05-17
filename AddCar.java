package com.ds.carpooling;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ds.carpooling.model.Car;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddCar extends AppCompatActivity {

    private EditText carModelName, carNo, licenceNo;
    private Button uploadLicence, addCar;
    private FirebaseAuth mAuth;
    private ImageView licenceImage;

    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        carModelName = findViewById(R.id.edt_carModel_name);
        carNo = findViewById(R.id.edt_car_NO);
        licenceNo = findViewById(R.id.edt_lic_NO);
        uploadLicence = findViewById(R.id.btn_upload_lic);
        licenceImage = findViewById(R.id.img_licence);
        addCar = findViewById(R.id.btn_add_car);
        mAuth = FirebaseAuth.getInstance();

       // mStorageRef = FirebaseStorage.getInstance().getReference("CarDetails");
       // mDatabaseRef = FirebaseDatabase.getInstance().getReference("CarDetails");


        uploadLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
                //openFileChooser();
            }
        });

        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCarDetails();
            }

            private void addCarDetails() {

                String carModel = carModelName.getText().toString().trim();
                String carNumber = carNo.getText().toString().trim();
                String licenceNumber = licenceNo.getText().toString().trim();
                //String licenceImg = licenceNumber;

                if (carModel.isEmpty()) {
                    carModelName.setError("Car Model name is Required");
                    carModelName.requestFocus();
                    return;
                }

                if (carNumber.isEmpty()) {
                    carNo.setError("Car Number is Required");
                    carNo.requestFocus();
                    return;
                }

                if (licenceNumber.isEmpty()) {
                    licenceNo.setError("Licence Number is Required");
                    licenceNo.requestFocus();
                    return;
                }


                if (imageUri != null) {
                    //uploadToFirebase(imageUri);
                    DatabaseReference root = FirebaseDatabase.getInstance().getReference("CarDetails").child(mAuth.getCurrentUser().getUid());
                    StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Car car=new Car(carModel,carNumber,licenceNumber,uri.toString());
                                    //String modelId= root.push().getKey();
                                    root.setValue(car);
                                    Toast.makeText(AddCar.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddCar.this, "Uploading Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AddCar.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }


                /*if (imageUri != null ){
                    StorageReference fileReference = mStorageRef.child(licenceNumber
                    +"."+getFileExtension(mImageUri));

                    fileReference.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Toast.makeText(AddCar.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                                    Car car = new Car(carModel,carNumber,licenceNumber,taskSnapshot.toString());

                                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                                    DatabaseReference DBRef=database.getReference("CarDetails").child(mAuth.getCurrentUser().getUid());
                                    DBRef.setValue(car).addOnCompleteListener(task -> {

                                        if (task.isSuccessful()){
                                            Toast.makeText(AddCar.this, "Car Added Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(AddCar.this, "Car is not Added. Please try Again!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddCar.this, e.getMessage().toString().trim(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(AddCar.this, "No files Selected", Toast.LENGTH_SHORT).show();
                }*/

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            licenceImage.setImageURI(imageUri);
        }
    }




    /*private void openFileChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(licenceImage);
         }
    }*/

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}