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
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PendingPaymentListActivity extends AppCompatActivity {

    private RecyclerView rvPendingPaymentList;

    private PedingPaymentAdapter adapter;
    private List<Bill> list;


    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private String userId;

    private String TAG = "PendingPaymentListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_payment_list);

        rvPendingPaymentList = findViewById(R.id.rvPendingPaymentList);
        rvPendingPaymentList.setHasFixedSize(true);
        rvPendingPaymentList.setLayoutManager(new LinearLayoutManager(this));

        fStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        list = new ArrayList<>();
        adapter = new PedingPaymentAdapter(this, list);
        rvPendingPaymentList.setAdapter(adapter);

        userId = auth.getCurrentUser().getUid();
        
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
                                        .collection("Bills").whereEqualTo("status", 1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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