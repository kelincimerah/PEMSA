package com.example.pemsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.pemsa.models.Bill;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HistoryPaymentListActivity extends AppCompatActivity {


    private RecyclerView rvHistory;

    private HistoryPaymentAdapter adapter;
    private List<Bill> list;


    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private String userId;

    private StorageReference storageReference;

    private String TAG = "HistoryPaymentListActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_payment_list);

        rvHistory = findViewById(R.id.rvHistoryPaymentAllUser);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        fStore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        adapter = new HistoryPaymentAdapter(this, list);
        rvHistory.setAdapter(adapter);

        showData();
    }

    private void showData() {

        fStore.collection("users").whereEqualTo("type", 0).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> listt = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : listt) {

                                fStore.collection("CurrentUser").document(snapshot.getString("id"))
                                        .collection("Bills").whereEqualTo("status", 2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            Log.d(TAG, "sukses1");
                                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                                                String documentId = documentSnapshot.getId();

                                                Bill bill = documentSnapshot.toObject(Bill.class);

                                                bill.setDocumentId(documentId);

                                                list.add(bill);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Log.d(TAG, "error saat ambil data");
                                    }
                                });

                            }

                        }

                    }
                });
    }
}