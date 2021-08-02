package com.example.pemsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.pemsa.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DataUserActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDataAlluser;

    private DataAllUserAdapter adapter;
    private List<User> list;

    private FirebaseFirestore fStore;
    private StorageReference storageReference;

    private String TAG = "DataUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_user);

        recyclerViewDataAlluser = findViewById(R.id.rvAlluser);
        recyclerViewDataAlluser.setHasFixedSize(true);
        recyclerViewDataAlluser.setLayoutManager(new LinearLayoutManager(this));

        fStore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        adapter = new DataAllUserAdapter(this, list);
        recyclerViewDataAlluser.setAdapter(adapter);
        storageReference = FirebaseStorage.getInstance().getReference();


        showData();
    }

    private void showData() {

        fStore.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listt = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : listt) {

                                String pathImage = "users/"+snapshot.getId()+"/Profile";

                                storageReference.child(pathImage).getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Log.d(TAG, "id : " +snapshot.getId());
                                                list.add(new User(snapshot.getString("fullName"), snapshot.getString("email"), snapshot.getString("address"), snapshot.getString("phoneNum"), uri.toString() ,snapshot.getLong("type").intValue()));
                                                adapter.notifyDataSetChanged();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        String urlFromInter = "https://i.postimg.cc/qv1sHg3y/profile32.jpg";
                                        list.add(new User(snapshot.getString("fullName"), snapshot.getString("email"), snapshot.getString("address"), snapshot.getString("phoneNum"), urlFromInter ,snapshot.getLong("type").intValue()));
                                        adapter.notifyDataSetChanged();
                                        Log.d(TAG, "OnFailed: failed show image ");
                                    }
                                });

                            }
                        }
                    }
                });


    }
}