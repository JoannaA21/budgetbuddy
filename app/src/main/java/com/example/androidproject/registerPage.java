package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class registerPage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


    }



    //Triggered when Login textView is clicked. Screen redirects to Login page
    public void LoginPage(View view) {
        Intent intent = new Intent(this, loginPage.class);
        startActivity(intent);
    }
}
