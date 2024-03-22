package com.example.androidproject;

import static com.example.androidproject.IntentUtils.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class profilePage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Call the method from IntentUtils to set up click listeners for navigation buttons
        setNavigationButtonClickListeners(this);

    }

    //Triggered when Login textView is clicked. Screen redirects to Login page
    public void ProfilePage(View view) {
        Intent intent = new Intent(this, profilePage.class);
        startActivity(intent);
    }


}
