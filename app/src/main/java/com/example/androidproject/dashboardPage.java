package com.example.androidproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidproject.databinding.DashboardBinding;

public class dashboardPage  extends AppCompatActivity {

    DashboardBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment( new dashboard_Fragment());

            binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.goal) {
                    replaceFragment(new profile());
                } else if (item.getItemId() == R.id.dashboard) {
                    replaceFragment(new dashboard_Fragment());
                } else if (item.getItemId() == R.id.expense) {
                    replaceFragment(new profile());
                } else if (item.getItemId() == R.id.profile) {
                    replaceFragment(new profile());
                }
                return true;
            });
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
