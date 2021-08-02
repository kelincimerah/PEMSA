package com.example.pemsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ProfilRtActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView fullnameTextView, emailTextView, addressTextView, phoneNumTextView, changePasswordTextView;
    private Button singOut;
    private ImageView userImageView, editProfileTextView;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;

    private StorageReference storageReference;

    private String TAG = "ProfilRtActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_rt2);

        fullnameTextView = findViewById(R.id.fullNameProfileRt);
        emailTextView = findViewById(R.id.emailProfileRt);
        changePasswordTextView = findViewById(R.id.changePasswordProfileRt);
        changePasswordTextView.setOnClickListener(this);
        addressTextView = findViewById(R.id.addressProfileRt);
        phoneNumTextView = findViewById(R.id.phoneNumProfileRt);
        singOut = findViewById(R.id.singOutProfileRt);
        singOut.setOnClickListener(this);
        userImageView = findViewById(R.id.ivProfileRt);
        editProfileTextView = findViewById(R.id.changeProfileProfileRt);
        editProfileTextView.setOnClickListener(this);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/Profile");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userImageView);
            }
        });

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    fullnameTextView.setText(documentSnapshot.getString("fullName"));
                    emailTextView.setText(documentSnapshot.getString("email"));
                    addressTextView.setText(documentSnapshot.getString("address"));
                    phoneNumTextView.setText(documentSnapshot.getString("phoneNum"));

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
            case R.id.singOutProfileRt:
                SingOut();
                break;
            case R.id.changePasswordProfileRt:
                Log.d(TAG, "masuk switch case");
                startActivity(new Intent(getApplicationContext(), ChangePasswordUserActivity.class));
                break;
            case R.id.changeProfileProfileRt:
                Intent i = new Intent(v.getContext(), EditProfileCitizenActivity.class);
                i.putExtra("fullName", fullnameTextView.getText().toString());
                i.putExtra("email", emailTextView.getText().toString());
                i.putExtra("address", addressTextView.getText().toString());
                i.putExtra("phoneNum", phoneNumTextView.getText().toString());
                startActivity(i) ;
                break;
        }
    }

    private void SingOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), SingInActivity.class));
        finish();
    }
}