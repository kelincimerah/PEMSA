package com.example.pemsa;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileCitizen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileCitizen extends Fragment implements View.OnClickListener{

    View rootView;

    private TextView fullnameTextView, emailTextView, addressTextView, phoneNumTextView, changePasswordTextView;
    private ImageView userImageView, editProfileTextView;
    private Button buttonSignOut;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;

    private StorageReference storageReference;

    private String TAG = "ProfileCitizenFragment";




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileCitizen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileCitizen.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileCitizen newInstance(String param1, String param2) {
        ProfileCitizen fragment = new ProfileCitizen();
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

        rootView = inflater.inflate(R.layout.fragment_profile_citizen, container, false);

        InitView();

        return rootView;
    }

    private void InitView() {
        fullnameTextView = rootView.findViewById(R.id.fullNameProfileCitizen);
        emailTextView = rootView.findViewById(R.id.emailProfileCitizen);
        changePasswordTextView = rootView.findViewById(R.id.changePasswordProfileCitizen);
        changePasswordTextView.setOnClickListener(this);
        addressTextView = rootView.findViewById(R.id.addressProfileCitizen);
        phoneNumTextView = rootView.findViewById(R.id.phoneNumProfileCitizen);
        userImageView = rootView.findViewById(R.id.ivProfileCitizen);
        userImageView.setOnClickListener(this);
        editProfileTextView = rootView.findViewById(R.id.changeProfileProfileCitizen);
        editProfileTextView.setOnClickListener(this);


        buttonSignOut = rootView.findViewById(R.id.singOutProfileCitizen);
        buttonSignOut.setOnClickListener(this);

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
            case R.id.singOutProfileCitizen:
                SingOut();
                break;
            case R.id.changeProfileProfileCitizen:
                Intent i = new Intent(v.getContext(), EditProfileCitizenActivity.class);
                i.putExtra("fullName", fullnameTextView.getText().toString());
                i.putExtra("email", emailTextView.getText().toString());
                i.putExtra("address", addressTextView.getText().toString());
                i.putExtra("phoneNum", phoneNumTextView.getText().toString());
                startActivity(i);
                break;
            case R.id.changePasswordProfileCitizen:
                startActivity(new Intent(getActivity(), ChangePasswordUserActivity.class));
        }

    }

    private void SingOut() {
        fAuth.signOut();
        startActivity(new Intent(getActivity(), SingInActivity.class));
    }
}