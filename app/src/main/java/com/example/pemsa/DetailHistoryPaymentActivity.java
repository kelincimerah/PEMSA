package com.example.pemsa;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pemsa.models.Bill;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailHistoryPaymentActivity extends AppCompatActivity {

    private TextView txtViewFullName, txtViewEmail, txtViewAddress, txtViewAmounBill, txtViewDateOfBill;
    private Button btnApprove;
    private ImageView ivStruckPatment;

    private String pathImage;

    private StorageReference storageReference;

    private Bill bill = null;

    private String TAG = "DetailHistoryPaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_payment);

        final Object object = getIntent().getSerializableExtra("historyData");

        if (object instanceof Bill){
            bill = (Bill) object;
        }

        txtViewFullName = findViewById(R.id.fullNameHistory);
        txtViewEmail = findViewById(R.id.emailHistory);
        txtViewAddress = findViewById(R.id.addressHistory);
        txtViewAmounBill = findViewById(R.id.amountBillHistory);
        txtViewDateOfBill = findViewById(R.id.dateOfBIllHistory);
        ivStruckPatment = findViewById(R.id.imageViewStruckHistory);

        if (bill != null){


            txtViewFullName.setText(bill.getName());
            txtViewEmail.setText(bill.getEmail());
            txtViewAddress.setText(bill.getAddress());
            txtViewAmounBill.setText(String.valueOf(bill.getBill()));
            txtViewDateOfBill.setText(bill.getDateOfBill());

            pathImage = bill.getUri();

            Log.d(TAG, "coba path image :" + pathImage);
        }

        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child(pathImage);


        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ivStruckPatment);
            }
        });
    }
}