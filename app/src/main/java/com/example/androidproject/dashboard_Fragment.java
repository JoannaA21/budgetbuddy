package com.example.androidproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//import com.anychart.AnyChart;
//import com.anychart.AnyChartView;
//import com.anychart.chart.common.dataentry.DataEntry;
//import com.anychart.chart.common.dataentry.ValueDataEntry;
//import com.anychart.charts.Pie;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dashboard_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dashboard_Fragment extends Fragment {


    private TextView curMonthText;
    private TextView goalInfoText;
    public double total_Expenses;
    public double total_goals;
    public double total_income;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public dashboard_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dashboard_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dashboard_Fragment newInstance(String param1, String param2) {
        dashboard_Fragment fragment = new dashboard_Fragment();
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

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard_, container, false);

        PieChart pieChart = rootView.findViewById(R.id.chart);
        TableLayout tableLayout = rootView.findViewById(R.id.expenseDataTable);
        curMonthText = rootView.findViewById(R.id.currentMonthText);
        goalInfoText = rootView.findViewById(R.id.goalInfo);
        curMonthText.setText(getCurrentMonth());
        TableRow row = null;
        String id = null;
        ArrayList<PieEntry> entries = new ArrayList<>();


        //For- after login
        try {
            JSONObject credentialIS = Utilities.getInternalStorage(getActivity(),"credential.txt");
            JSONObject retrieve = credentialIS.getJSONObject("details");
            id = retrieve.getString("id");
            Log.d("user", "info " + credentialIS.getString("details"));
            Log.d("creds", "Creds id: " + retrieve.getString("id") +" " + retrieve.getString("email"));
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }


        GetExpenseRequestAsynTask readExpenseTask = new GetExpenseRequestAsynTask();
        readExpenseTask.execute("http://143.198.237.154:3001/api/getuserexpense/" + id);
//
        GetGoalsRequestAsynTask readGoalsTask = new GetGoalsRequestAsynTask();
        readGoalsTask.execute("http://143.198.237.154:3001/api/getusergoal/" + id);
//
        GetSavingsRequestAsynTask readTask = new GetSavingsRequestAsynTask();
        readTask.execute("http://143.198.237.154:3001/api/getuserincome/" + id);

        if (Utilities.isFileExists(getActivity(),"profileInfo.txt")){
            try{
                JSONObject profilegoals = Utilities.getInternalStorage(getActivity(),"profileInfo.txt");

                double savings = profilegoals.getInt("current_balance");
                total_income = profilegoals.getDouble("monthly_income");

                row = addDataToTable("Current Savings", savings, "savings");
                tableLayout.addView(row);

            }catch (JSONException e){
                throw new RuntimeException(e);
            }
        }

        if (Utilities.isFileExists(getActivity(),"goals.txt")) {
            String jsonString = String.valueOf(Utilities.getJSONArrayFromInternalStorage(getActivity(),"goals.txt"));

            Log.d("This is the goals", jsonString);

            try {
                JSONArray goalsArray = new JSONArray(jsonString);
                row = addDataToTable("      TARGET SAVINGS",0.0,"");
                tableLayout.addView(row);

                for (int i = 0; i < goalsArray.length(); i++) {
                    JSONObject goalObject = goalsArray.getJSONObject(i);
                    // Extract goal_type and amount from each goals object
                    String goalType =  goalObject.getString("goal_type");
                    double amount = goalObject.getDouble("amount_goal");

                    total_goals += amount;
                    row = addDataToTable(goalType, total_goals, "goals");
                    tableLayout.addView(row);
                }

                if(total_income == 0  && total_Expenses == 0 && total_goals ==0) {
                    goalInfoText.setText("START TRACKING YOUR FINANCES BY SETTING UP YOUR INCOME, GOALS, AND BY ADDING YOUR EXPENSES");
                    goalInfoText.setTextColor(Color.parseColor("#000000"));
                }
                else if((total_income - total_Expenses) > total_goals){
                    goalInfoText.setText("YOU ARE ON TRACK ON ACHIEVING YOUR MONTHLY SAVINGS ");
                    goalInfoText.setTextColor(Color.parseColor("#157811"));
                    //goalInfoText.setText("YOU ARE ON TRACK ON ACHIEVING YOUR MONTHLY SAVINGS"+"\n"+ "Total_Income: "+ total_income +  "Total_Expense: " + total_Expenses + "Total Goals: "+ total_goals);
                }else{
                    double over = ((total_income - total_Expenses) - total_goals) * -1;
                    goalInfoText.setText("YOU ARE " + over + " OVER THE BUDGET" + "\n" + "YOUR MONTHLY SAVINGS GOAL SHOULD BE " + total_goals);
                    // goalInfoText.setText("YOU ARE " + over + " OVER THE BUDGET" +"\n"+ "Total_Income: "+ total_income +  "Total_Expense: " + total_Expenses + "Total Goals: "+ total_goals);
                    goalInfoText.setTextColor(Color.parseColor("#db0c26"));
                    Log.d("Expense", "Total_Income: "+ total_income +  "Total_Expense: " + total_Expenses + "Total Goals: "+ total_goals);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Check if the expenses file exists
        if (Utilities.isFileExists(getActivity(),"expenses.txt")) {
           // Log.d("Debug", "There is expenses.txt");
            String jsonString = String.valueOf(Utilities.getJSONArrayFromInternalStorage(getActivity(),"expenses.txt"));
            //Log.d("JSON Content", "Content from file: " + jsonString);
            row = addDataToTable("      EXPENSES",0.0,"");
            tableLayout.addView(row);

            try {
                JSONArray expensesArray = new JSONArray(jsonString);
                for (int i = 0; i < expensesArray.length(); i++) {
                    JSONObject expenseObject = expensesArray.getJSONObject(i);
                    // Extract expense_type and cost from each expense object
                    String expenseType =  expenseObject.getString("expense_type");
                    double cost = expenseObject.getDouble("cost");
                    // Create a PieEntry with the extracted data
                    entries.add(new PieEntry((float) cost, expenseType));
                    row = addDataToTable(expenseType,cost,"expense");
                    total_Expenses += cost;

                  // Add the TableRow to the TableLayout
                    tableLayout.addView(row);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }





        PieDataSet pieDataSet = new PieDataSet(entries,"");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();



        return rootView;
    }


    private String getCurrentMonth() {

        String month[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        try {
            int monthIndex = Integer.parseInt(dateFormat.format(date)) - 1;
            return month[monthIndex];
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return ""
                ;
    }

    private TableRow addDataToTable(String expense_type, Double cost, String type){
        TableRow row = new TableRow(getContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
//                    layoutParams.setMargins(10,10,10,10);
        row.setLayoutParams(layoutParams);

        // Create TextView for expense type
        TextView expenseTypeTextView = new TextView(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
//                    params.setMargins(30,30,30,30);
        expenseTypeTextView.setText(expense_type);
        expenseTypeTextView.setHeight(dpToPx(getContext(), 50));
        expenseTypeTextView.setWidth(dpToPx(getContext(), 180));
        expenseTypeTextView.setTextColor(Objects.equals(type, "") ?Color.BLACK: Color.WHITE);
        expenseTypeTextView.setBackgroundColor(Objects.equals(type, "expense") ?Color.parseColor("#af5852"):Objects.equals(type, "savings") ? Color.parseColor("#168118"):Objects.equals(type, "goals") ? Color.parseColor("#008b8b"): Color.parseColor("#FFFFFF"));
        expenseTypeTextView.setPadding(dpToPx(getContext(), 10), dpToPx(getContext(), 5), dpToPx(getContext(), 10), dpToPx(getContext(), 5));
        expenseTypeTextView.setTextSize(16);
        expenseTypeTextView.setTypeface(null, Typeface.BOLD);
        expenseTypeTextView.setGravity(Gravity.START);
        row.addView(expenseTypeTextView);

        // Create TextView for expense cost
        TextView expenseCostTextView = new TextView(getContext());
        expenseCostTextView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        expenseCostTextView.setText(cost != 0.0 ? String.valueOf(cost): "");
        expenseCostTextView.setTextColor(Objects.equals(type, "expense") ?Color.BLACK: Color.parseColor("#157811"));
        expenseCostTextView.setPadding(dpToPx(getContext(), 15), dpToPx(getContext(), 10), dpToPx(getContext(), 10), dpToPx(getContext(), 5));
        expenseCostTextView.setTextSize(22);
        expenseCostTextView.setTypeface(null, Typeface.BOLD_ITALIC);
        expenseCostTextView.setGravity(Gravity.START);
        row.addView(expenseCostTextView);

        // Add the TableRow to the TableLayout

        return row;


    }


    protected class GetExpenseRequestAsynTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = Utilities.fetchDataFromUrl(urls[0]);
            Log.d("Expense Debg", response);
            return response;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            Log.d("Expense Debg on post Exeutr", jsonData);
            if (jsonData != null) {
                try {
                    // Check if the JSON data is an array or an object
                    if (jsonData.startsWith("[")) {
                        // If it's an array, parse it as a JSONArray
                        JSONArray jsonArray = new JSONArray(jsonData);

                        // Create a JSONArray to store expense objects
                        JSONArray expensesArray = new JSONArray();


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Log.d("Expense Debg in for loop", String.valueOf(jsonObject.length()));

                            // Extract expense_type and cost
                            String expenseType = jsonObject.getString("expense_type") != null ? jsonObject.getString("expense_type"): "None";
                            Log.d("Expense Debg in for loop", expenseType);
                            double cost =  0.0;
                            if(jsonObject.get("cost") instanceof Integer){
                                 cost = jsonObject.getDouble("cost");
                            } else if (jsonObject.get("cost") instanceof String) {
                                 cost = 0.00;
                            }
                           // double cost = jsonObject.getDouble("cost") != 0 ? jsonObject.getDouble("cost"): 0;
                            Log.d("Expense Debg in for loop", String.valueOf(cost));

                            JSONObject expenseObject = new JSONObject();
                            expenseObject.put("expense_type", expenseType);
                            expenseObject.put("cost", cost);

                            // Add the expense object to the expenses array
                            expensesArray.put(expenseObject);


                        }
                        Log.d("ExpensesArray", " " + expensesArray.toString());
                        Utilities.writeToInternalStorage(getActivity(),"expenses.txt", expensesArray.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Unable to connect.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    protected class GetSavingsRequestAsynTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            String response = Utilities.fetchDataFromUrl(urls[0]);
            Log.d("Expense Debg", response);
            return response;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            if (jsonData != null) {
                try {
                    // Check if the JSON data is an array or an object
                    if (jsonData.startsWith("[")) {
                        // If it's an array, parse it as a JSONArray
                        JSONArray jsonArray = new JSONArray(jsonData);
                        Double monthly_income, all_savings, expenses, current_balance;
                        JSONObject profile = new JSONObject();
                        // Iterate over the elements of the JSONArray
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // Get the JSONObject at index i
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Access the values from the JSONObject
                            monthly_income = jsonObject.getDouble("monthly_income");
                            all_savings = jsonObject.getDouble("all_savings");
                            expenses = jsonObject.getDouble("expenses");
                            current_balance = jsonObject.getDouble("current_balance");

                            // Do something with the values
                            profile.put("monthly_income", monthly_income);
                            profile.put("all_savings", all_savings);
                            profile.put("expenses", expenses);
                            profile.put("current_balance", current_balance);
                        }
                        Log.d("JSONArray", " " + profile);
                        Utilities.writeToInternalStorage(getActivity(),"profileInfo.txt", profile.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                }
            } else {
                // Show error message or handle null response here
                Toast.makeText(getActivity(), "Unable to connect.", Toast.LENGTH_SHORT).show();
            }
        }


    }


    protected class GetGoalsRequestAsynTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = Utilities.fetchDataFromUrl(urls[0]);
            Log.d("Expense Debg", response);
            return response;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            Log.d("Expense Debg on post Exeutr", jsonData);
            if (jsonData != null) {
                try {
                    // Check if the JSON data is an array or an object
                    if (jsonData.startsWith("[")) {
                        // If it's an array, parse it as a JSONArray
                        JSONArray jsonArray = new JSONArray(jsonData);

                        // Create a JSONArray to store the goal objects
                        JSONArray goalArray = new JSONArray();

                        // Iterate over the elements of the JSONArray
                        for (int i = 0; i < jsonArray.length(); i++) {
                            // Get the JSONObject at index i
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Extract relevant data from the JSONObject
                            String goalType = jsonObject.getString("goal_type");
                            double amount = jsonObject.getDouble("amount_goal");

                            // Create a JSONObject to store the goal data
                            JSONObject goalObject = new JSONObject();
                            goalObject.put("goal_type", goalType);
                            goalObject.put("amount_goal", amount);

                            // Add the goalObject to the goalArray
                            goalArray.put(goalObject);
                        }

                        // Log the goals array for debugging
                        Log.d("GoalsArray", goalArray.toString());

                        // Write the goals array to internal storage
                        Utilities.writeToInternalStorage(getActivity(), "goals.txt", goalArray.toString());
                    } else {
                        // If it's not an array, it's a JSONObject
                        // Parse the JSON response as a JSONObject
                        JSONObject jsonResponse = new JSONObject(jsonData);

                        // Extract relevant data from the JSONObject
                        String goalType = jsonResponse.getString("goal_type");
                        double amount = jsonResponse.getDouble("amount_goal");

                        // Create a JSONArray to store the goal object
                        JSONArray goalArray = new JSONArray();
                        JSONObject goalObject = new JSONObject();
                        goalObject.put("goal_type", goalType);
                        goalObject.put("amount_goal", amount);
                        goalArray.put(goalObject);

                        // Log the goals array for debugging
                        Log.d("GoalsArray", goalArray.toString());

                        // Write the goals array to internal storage
                        Utilities.writeToInternalStorage(getActivity(), "goals.txt", goalArray.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Show error message if response is null
                Toast.makeText(getActivity(), "Unable to connect.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static int dpToPx(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}