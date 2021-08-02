package com.example.pemsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView btmView;

    boolean isPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragment(new DashboardCitizen());


        btmView = findViewById(R.id.bottomViewCitizens);
        btmView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                if (item.getItemId() == R.id.dashboardCitizen){
                    getFragment(new DashboardCitizen());
                }else if (item.getItemId() == R.id.profilCitizen){
                    getFragment(new ProfileCitizen());
                }

                return false;
            }
        });


    }

    private void getFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        if (btmView.getSelectedItemId()==R.id.dashboardCitizen && isPressed){
//            super.onBackPressed();
//            finish();
            finishAffinity();
            System.exit(0);
        }else {
            btmView.setSelectedItemId(R.id.dashboardCitizen);
            Toast.makeText(getApplicationContext(), "Silhkan klik lagi untuk keluar dari Applikasi", Toast.LENGTH_SHORT).show();
            isPressed = true;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                isPressed = false;
            }
        };

        new Handler().postDelayed(runnable, 2000);

    }
}