package com.example.pemsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pemsa.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SendBillActivity extends AppCompatActivity {

    private TextView txtViewFullName, txtViewEmail, txtViewAddress, txtViewPhoneNumber;
    private Button btnSendBill;

    private String TAG = "SendBillActivity";

    private String fullName, email, address, phoneNumberr;

    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private StorageReference storageReference;

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_bill);

        final Object object = getIntent().getSerializableExtra("citizenData");

        if (object instanceof User){
            user = (User) object;
        }

        txtViewFullName = findViewById(R.id.fullNameSendBill);
        txtViewEmail = findViewById(R.id.emailSendBill);
        txtViewAddress = findViewById(R.id.addressSendBill);
        txtViewPhoneNumber = findViewById(R.id.phoneNumSendBill);
        btnSendBill = findViewById(R.id.btnSendBill);

        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        if (user != null){
            fullName = user.getFullName();
            email = user.getEmail();
            address = user.getAddress();
            phoneNumberr = user.getPhone();
            txtViewFullName.setText(fullName);
            txtViewEmail.setText(email);
            txtViewAddress.setText(address);
            txtViewPhoneNumber.setText(phoneNumberr);

            Log.d(TAG, "tesst id : "+user.getId());
        }

        btnSendBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBill();
            }
        });

    }

    private void sendBill() {

        String saveCurrentDate, saveCurrentTime;
        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final HashMap<String,Object> billCitizen = new HashMap<>();

        billCitizen.put("idCitizen", user.getId());
        billCitizen.put("name", fullName);
        billCitizen.put("email", email);
        billCitizen.put("address", address);
        billCitizen.put("bill", 20000);
        billCitizen.put("status", 0);
        billCitizen.put("dateOfBill", saveCurrentDate);
        billCitizen.put("uri", "null");
        billCitizen.put("currentDate", saveCurrentDate);
        billCitizen.put("currentTime", saveCurrentTime);

        fStore.collection("CurrentUser").document(user.getId())
                .collection("Bills").add(billCitizen).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                Toast.makeText(SendBillActivity.this, "Tagihan berhasil dikirim", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

}