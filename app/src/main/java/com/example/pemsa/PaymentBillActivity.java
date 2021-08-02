package com.example.pemsa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pemsa.models.Bill;
import com.example.pemsa.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.security.PrivateKey;
import java.util.HashMap;

public class PaymentBillActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView amounBill, dateOfBill;
    private ImageView ivStruck;
    private Button btnPayment;

    private Uri imageUri;

    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    private String pathImage, date, time, dateBill;
    private int amountBill, status;

    private Bill bill = null;

    private String TAG = "SendBillActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_bill);

        final Object object = getIntent().getSerializableExtra("billData");

        if (object instanceof Bill){
            bill = (Bill) object;
        }

        amounBill = findViewById(R.id.amountbillPayment);
        dateOfBill = findViewById(R.id.dateOfBIllPayment);
        ivStruck = findViewById(R.id.imageViewStruck);
        ivStruck.setOnClickListener(this);
        btnPayment = findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(this);

        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        if (bill != null){
            amounBill.setText(String.valueOf(bill.getBill()));
            dateOfBill.setText(bill.getDateOfBill());

            Log.d(TAG, "Test doc id : "+ bill.getDocumentId());
            pathImage = "Struck/"+auth.getCurrentUser().getUid()+"/"+bill.getDocumentId();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewStruck:
                SelectImage();
                break;
            case R.id.btnPayment:
                PayBill();
                break;
        }
    }

    private void PayBill() {

        amountBill = bill.getBill();
        dateBill = bill.getDateOfBill();
        date = bill.getCurrentDate();
        time = bill.getCurrentTime();
        status = 1;


        final HashMap<String,Object> billCitizen = new HashMap<>();

        billCitizen.put("status", status);
        billCitizen.put("uri", pathImage);

        

        fStore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Bills").document(bill.getDocumentId()).set(billCitizen, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(PaymentBillActivity.this, "Data Updated!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(PaymentBillActivity.this, "Error : on data updated", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d(TAG, "errornya apa : "+ e.getMessage());
                Toast.makeText(PaymentBillActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SelectImage() {
        Intent openGaleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGaleryIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                imageUri = data.getData();

                UploadImageToFirebase(imageUri);
            }
        }
    }

    private void UploadImageToFirebase(Uri imageUri) {

        StorageReference fileref = storageReference.child(pathImage);
        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(ivStruck);
                        Toast.makeText(PaymentBillActivity.this, "Image Updated!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d(TAG, "Upload image failed on admin profile class");
                Toast.makeText(PaymentBillActivity.this, "Image Updated!!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}