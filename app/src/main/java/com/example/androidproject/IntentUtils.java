package com.example.androidproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class IntentUtils {

    public static Intent createGoalPageIntent(Context context) {
        return new Intent(context, goalPage.class);
    }

    public static Intent createProfilePageIntent(Context context) {
        return new Intent(context, profilePage.class);
    }

    public static Intent createExpensePageIntent(Context context) {
        return new Intent(context, expensePage.class);
    }

    public static void setNavigationButtonClickListeners(final Activity activity) {
        Button btnGoalPage = activity.findViewById(R.id.goalPage);
        Button btnProfilePage = activity.findViewById(R.id.profilePage);
        Button btnExpensePage = activity.findViewById(R.id.expensePage);

        btnGoalPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button Click", "GoalPage button clicked");
                Log.e("Button Click", "clicked!");
                activity.startActivity(createGoalPageIntent(activity));
            }
        });

        btnProfilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(createProfilePageIntent(activity));
            }
        });

        btnExpensePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(createExpensePageIntent(activity));
            }
        });
    }
}
