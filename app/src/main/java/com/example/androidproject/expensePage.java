package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class expensePage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_income);

        // Call the method from IntentUtils to set up click listeners for navigation buttons
        IntentUtils.setNavigationButtonClickListeners(this);


    }

    public void ExpensePage(View view) {
        Intent intent = new Intent(this, expensePage.class);
        startActivity(intent);
    }
}
