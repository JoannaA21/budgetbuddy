package com.example.androidproject;

import static com.example.androidproject.IntentUtils.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class goalPage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_goal);

        // Call the method from IntentUtils to set up click listeners for navigation buttons
        setNavigationButtonClickListeners(this);

    }

    public void GoalPage(View view) {
        Intent intent = new Intent(this, goalPage.class);
        startActivity(intent);
    }




}
