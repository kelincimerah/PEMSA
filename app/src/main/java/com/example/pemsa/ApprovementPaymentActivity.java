package com.example.pemsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pemsa.models.Bill;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ApprovementPaymentActivity extends AppCompatActivity {

    private TextView txtViewFullName, txtViewEmail, txtViewAddress, txtViewAmounBill, txtViewDateOfBill;
    private Button btnApprove;
    private ImageView ivStruckPatment;

    private String fullName, email, address, pathImage, date, time, dateBill;
    private int amountBill, status;

    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    private Bill bill = null;

    private String TAG = "SendBillActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approvement_payment);

        final Object object = getIntent().getSerializableExtra("pendingData");

        if (object instanceof Bill){
            bill = (Bill) object;
        }

        txtViewFullName = findViewById(R.id.fullNameApprove);
        txtViewEmail = findViewById(R.id.emailApprove);
        txtViewAddress = findViewById(R.id.addressApprove);
        txtViewAmounBill = findViewById(R.id.amountBillApprove);
        txtViewDateOfBill = findViewById(R.id.dateOfBIllApprove);
        ivStruckPatment = findViewById(R.id.imageViewStruckApprove);
        btnApprove = findViewById(R.id.btnApprove);


        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        status = 2;

        if (bill != null){

            fullName = bill.getName();
            email = bill.getEmail();
            address = bill.getAddress();
            amountBill = bill.getBill();
            dateBill = bill.getDateOfBill();
            date = bill.getCurrentDate();
            time = bill.getCurrentTime();


            txtViewFullName.setText(fullName);
            txtViewEmail.setText(email);
            txtViewAddress.setText(address);
            txtViewAmounBill.setText(String.valueOf(amountBill));
            txtViewDateOfBill.setText(dateBill);

            pathImage = bill.getUri();

            Log.d(TAG, "coba ambil id warga :" + bill.getIdCitizen());
        }

        StorageReference profileRef = storageReference.child(pathImage);


        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ivStruckPatment);
            }
        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApprovedPayment();
            }
        });
    }

    private void ApprovedPayment() {

        final HashMap<String,Object> billCitizen = new HashMap<>();

        billCitizen.put("status", status);

        fStore.collection("CurrentUser").document(bill.getIdCitizen())
                .collection("Bills").document(bill.getDocumentId()).set(billCitizen, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(ApprovementPaymentActivity.this, "Data Updated!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(ApprovementPaymentActivity.this, "Error : on data updated", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d(TAG, "errornya apa : "+ e.getMessage());
                Toast.makeText(ApprovementPaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}