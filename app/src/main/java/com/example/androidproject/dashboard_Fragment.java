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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dashboard_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dashboard_Fragment extends Fragment {


    private TextView curMonthText;


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
        curMonthText.setText(getCurrentMonth());
        String id = null;
        ArrayList<PieEntry> entries = new ArrayList<>();




        //For- after login
        try {
            JSONObject credentialIS = getInternalStorage("credential.txt");
            JSONObject retrieve = credentialIS.getJSONObject("details");
            id = retrieve.getString("id");
            Log.d("user", "info " + credentialIS.getString("details"));
            Log.d("creds", "Creds id: " + retrieve.getString("id") +" " + retrieve.getString("email"));
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }


        GetExpenseRequestAsynTask readTask = new GetExpenseRequestAsynTask();
        readTask.execute("http://143.198.237.154:3001/api/getuserexpense/" + id);

        // Check if the expenses file exists
        if (isFileExists("expenses.txt")) {
           // Log.d("Debug", "There is expenses.txt");
            String jsonString = String.valueOf(getJSONArrayFromInternalStorage("expenses.txt"));
            //Log.d("JSON Content", "Content from file: " + jsonString);

            try {
                JSONArray expensesArray = new JSONArray(jsonString);
                for (int i = 0; i < expensesArray.length(); i++) {
                    JSONObject expenseObject = expensesArray.getJSONObject(i);
                    // Extract expense_type and cost from each expense object
                    String expenseType =  expenseObject.getString("expense_type");
                    double cost = expenseObject.getDouble("cost");
                    // Create a PieEntry with the extracted data
                    entries.add(new PieEntry((float) cost, expenseType));
                    // Create a new TableRow
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
                    expenseTypeTextView.setText(expenseType);
                    expenseTypeTextView.setHeight(dpToPx(getContext(), 50));
                    expenseTypeTextView.setWidth(dpToPx(getContext(), 180));
                    expenseTypeTextView.setBackgroundColor(Color.parseColor("#239B90"));
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

                    expenseCostTextView.setText(String.valueOf(cost));
//                    expenseCostTextView.setTextColor(Color.BLACK);
                    expenseCostTextView.setPadding(dpToPx(getContext(), 15), dpToPx(getContext(), 10), dpToPx(getContext(), 10), dpToPx(getContext(), 5));
                    expenseCostTextView.setTextSize(22);
                    expenseCostTextView.setTypeface(null, Typeface.BOLD | Typeface.ITALIC);
                    expenseCostTextView.setGravity(Gravity.START);
                    row.addView(expenseCostTextView);

                    // Add the TableRow to the TableLayout
                    tableLayout.addView(row);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        PieDataSet pieDataSet = new PieDataSet(entries, "Budget");
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


    public boolean isFileExists(String fileName) {
        File path = getActivity().getFilesDir();
        File file = new File(path, fileName);
        return file.exists();
    }

    public JSONArray getJSONArrayFromInternalStorage(String fileName) {
        File path = getActivity().getFilesDir();
        File file = new File(path, fileName);
        byte[] content = new byte[(int) file.length()];

        try {
            FileInputStream stream = new FileInputStream(file);
            stream.read(content);
            String json = new String(content);

            if (json.trim().startsWith("[")) {
                return new JSONArray(json);
            } else {
                // Log an error or handle the invalid JSON content
                Log.e("JSON Parsing", "Invalid JSON array content");
                return new JSONArray();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public JSONObject getInternalStorage(String fileName) {
        File path = getActivity().getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content = new byte[(int) readFrom.length()];

        try {

            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            String json = new String(content);
            return new JSONObject(json);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    protected class GetExpenseRequestAsynTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null; //reads file line by line
            String response = null;

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                InputStream inputStream = urlConnection.getInputStream(); //read file
                StringBuilder buffer = new StringBuilder();

                if (inputStream == null) {
                    // Return null to indicate failure
                    return null;
                }

                //BufferReader reads JSON file from url
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                response = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                // Return null to indicate failure
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect(); //free up connection
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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

                        // Create a JSONArray to store expense objects
                        JSONArray expensesArray = new JSONArray();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Extract expense_type and cost
                            String expenseType = jsonObject.getString("expense_type");
                            double cost = jsonObject.getDouble("cost");


                            JSONObject expenseObject = new JSONObject();
                            expenseObject.put("expense_type", expenseType);
                            expenseObject.put("cost", cost);

                            // Add the expense object to the expenses array
                            expensesArray.put(expenseObject);
                        }
                        Log.d("ExpensesArray", " " + expensesArray.toString());
                        writeToInternalStorage("expenses.txt", expensesArray.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Unable to connect.", Toast.LENGTH_SHORT).show();
            }
        }

        public void writeToInternalStorage(String fileName, String content) {
            File path = getActivity().getFilesDir();

            FileOutputStream writer = null;
            try {
                writer = new FileOutputStream(new File(path, fileName));
                writer.write(content.getBytes());
                Log.d("Internal Storage", "Content written to " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error writing to internal storage", Toast.LENGTH_SHORT).show();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static int dpToPx(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}