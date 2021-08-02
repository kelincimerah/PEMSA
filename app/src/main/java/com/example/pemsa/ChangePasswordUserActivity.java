package com.example.pemsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class ChangePasswordUserActivity extends AppCompatActivity {

    private EditText editTextCurrentPassword, editTextNewPassword;
    private Button updateBtn;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private String TAG = "ChangePasswordUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_user);

        editTextCurrentPassword = findViewById(R.id.currentPassword);
        editTextNewPassword = findViewById(R.id.newPassword);
        updateBtn = (Button) findViewById(R.id.updatePassword);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = editTextCurrentPassword.getText().toString().trim();
                String newPassword = editTextNewPassword.getText().toString().trim();

                if(currentPassword.isEmpty()){
                    editTextCurrentPassword.setError("Current Password is empty!");
                    editTextCurrentPassword.requestFocus();
                    return;
                }

                if(newPassword.isEmpty()){
                    editTextNewPassword.setError("Current Password is empty!");
                    editTextNewPassword.requestFocus();
                    return;
                }

                if(newPassword.length()<6){
                    editTextCurrentPassword.setError("New Password length must atleast 6 character!");
                    editTextCurrentPassword.requestFocus();
                    return;
                }

                updatePassword(currentPassword, newPassword);
            }
        });
    }

    private void updatePassword(String currentPassword, String newPassword) {

        FirebaseUser user = fAuth.getCurrentUser();

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ChangePasswordUserActivity.this, "Succes update Password", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(ChangePasswordUserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "Update password Failed");

                                    }
                                });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ChangePasswordUserActivity.this, "Wrong Current Password", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Update password Failed");
            }
        });
    }
}