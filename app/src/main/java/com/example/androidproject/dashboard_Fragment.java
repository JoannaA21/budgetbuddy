package com.example.androidproject;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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


//    AnyChartView anyChartView;
//    String[] expense = {"Rent", "Grocery", "Food", "Car", "Travel", "Savings","Phone", "Misc."};
//    int[] cost = {750,300, 100, 400,100, 500, 600, 50, 200};
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard_, container, false);

        PieChart pieChart = rootView.findViewById(R.id.chart);
        curMonthText = rootView.findViewById(R.id.currentMonthText);

        curMonthText.setText(getCurrentMonth());


        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add( new PieEntry(600f, "Savings"));
        entries.add( new PieEntry(550f, "Rent"));
        entries.add( new PieEntry(290f, "Car"));
        entries.add( new PieEntry(300f, "Grocery"));
        entries.add( new PieEntry(100f, "Travel"));
        entries.add( new PieEntry(50f, "Phone"));
        entries.add( new PieEntry(150f, "Misc."));

        PieDataSet pieDataSet = new PieDataSet(entries, "Budget");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();

//        anyChartView = rootView.findViewById(R.id.chart);
//        setupChartView();



        return rootView;
    }


    private String getCurrentMonth (){

        String month[] ={"January", "February", "March", "April", "May", "June", "July", "August", "September", "October","November", "December"};
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        try {
            int monthIndex = Integer.parseInt(dateFormat.format(date)) - 1;
            return month[monthIndex];
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return ""
;    }

//    private void setupChartView() {
//
//        Pie pie = AnyChart.pie();
//        List<DataEntry> dataEntries = new ArrayList<>();
//
//        for(int i = 0; i<expense.length;i++){
//            dataEntries.add(new ValueDataEntry(expense[i],cost[i]));
//        }
//
//        pie.data(dataEntries);
//        pie.title("Monthly Expenses");
//        anyChartView.setChart(pie);
//    }
}