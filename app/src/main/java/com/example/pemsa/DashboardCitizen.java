package com.example.pemsa;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pemsa.models.Bill;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardCitizen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardCitizen extends Fragment {

    View rootView;

    private ImageView userImage;
    private RecyclerView rvPayment, rvHistoryPayment;
    private TextView fullName;

    private BillPaymentAdapter adapter;
    private HistoryPaymentAdapter adapterHistory;
    private List<Bill> list;
    private List<Bill> list2;


    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private String userId;


    private StorageReference storageReference;

    private String TAG = "DashboardCitizen";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardCitizen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardCitizen.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardCitizen newInstance(String param1, String param2) {
        DashboardCitizen fragment = new DashboardCitizen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_dashboard_citizen, container, false);

        InitView();

        return rootView;
    }

    private void InitView() {

        userImage = rootView.findViewById(R.id.citizenImageDashboard);
        fullName = rootView.findViewById(R.id.fullNameDashboardCitizen);

        rvPayment = rootView.findViewById(R.id.rvPaymentDashboard);
        rvPayment.setHasFixedSize(true);
        rvPayment.setLayoutManager(new LinearLayoutManager(getContext()));

        rvHistoryPayment = rootView.findViewById(R.id.rvHistoryPaymentDasboard);
        rvHistoryPayment.setHasFixedSize(true);
        rvHistoryPayment.setLayoutManager(new LinearLayoutManager(getContext()));

        fStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        list = new ArrayList<>();
        adapter = new BillPaymentAdapter(getContext(), list);
        rvPayment.setAdapter(adapter);

        list2 = new ArrayList<>();
        adapterHistory = new HistoryPaymentAdapter(getContext(), list2);
        rvHistoryPayment.setAdapter(adapterHistory);

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

        showDataBill();
        ShowHistoryData();

    }

    private void ShowHistoryData() {

        fStore.collection("CurrentUser").document(userId)
                .collection("Bills").whereEqualTo("status", 2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "sukses1");
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){

                        String documentId = documentSnapshot.getId();

                        Bill bill = documentSnapshot.toObject(Bill.class);

                        bill.setDocumentId(documentId);

                        list2.add(bill);
                        adapterHistory.notifyDataSetChanged();
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

    private void showDataBill() {

        fStore.collection("CurrentUser").document(userId)
                .collection("Bills").whereNotEqualTo("status", 2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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