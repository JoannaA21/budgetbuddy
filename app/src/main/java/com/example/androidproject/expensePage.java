package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class expensePage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_income);


    }

    public void GoalPage(View view) {
        Intent intent = new Intent(this, goalPage.class);
        startActivity(intent);
    }
}
