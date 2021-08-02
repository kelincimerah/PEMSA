package com.example.pemsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class DashboardRtActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imgViewBill, imgViewPendingPay, imgViewHistoryPay, imgViewDataUser, imgViewProfile;
    private ImageView userImage;
    private TextView fullName;

    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private String userId;


    private StorageReference storageReference;

    private String TAG = "DashboardRtActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_rt);

        userImage = findViewById(R.id.ivUserDashboardRt);
        fullName = findViewById(R.id.fullNameDashboardRt);

        imgViewBill = findViewById(R.id.reminderPaymentDashboardAdmin);
        imgViewBill.setOnClickListener(this);
        imgViewPendingPay = findViewById(R.id.pendingDashboardAdmin);
        imgViewPendingPay.setOnClickListener(this);
        imgViewHistoryPay = findViewById(R.id.historyPaymentDashboardAdmin);
        imgViewHistoryPay.setOnClickListener(this);
        imgViewDataUser = findViewById(R.id.ivAllUserDashboardAdmin);
        imgViewDataUser.setOnClickListener(this);
        imgViewProfile = findViewById(R.id.profileDashboardRt);
        imgViewProfile.setOnClickListener(this);

        fStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId = auth.getCurrentUser().getUid();
        StorageReference profileRef = storageReference.child("users/"+userId+"/Profile");


        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userImage);
            }
        });



        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    fullName.setText(documentSnapshot.getString("fullName"));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d(TAG, "error saat ambil data");
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.reminderPaymentDashboardAdmin:
                startActivity(new Intent(getApplicationContext(), ChooseCitizen.class));
                break;
            case R.id.pendingDashboardAdmin:
                startActivity(new Intent(getApplicationContext(), PendingPaymentListActivity.class));
                break;
            case R.id.historyPaymentDashboardAdmin:
                startActivity(new Intent(getApplicationContext(), HistoryPaymentListActivity.class));
                break;
            case R.id.ivAllUserDashboardAdmin:
                startActivity(new Intent(getApplicationContext(), DataUserActivity.class));
                break;
            case R.id.profileDashboardRt:
                startActivity(new Intent(getApplicationContext(), ProfilRtActivity.class));
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}