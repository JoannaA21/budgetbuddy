package com.example.androidproject;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_Fragment newInstance(String param1, String param2) {
        profile_Fragment fragment = new profile_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView user_fullName, user_Email, profile_monthlyIncome;
        TextView profile_allSavings, profile_expenses, profile_currentBalance;
        user_fullName = view.findViewById(R.id.user_fullName);
        user_Email = view.findViewById(R.id.user_Email);
        profile_monthlyIncome = view.findViewById(R.id.profile_monthlyIncome);
        profile_allSavings = view.findViewById(R.id.profile_allSavings);
        profile_expenses = view.findViewById(R.id.profile_expenses);
        profile_currentBalance = view.findViewById(R.id.profile_currentBalance);
        String id = null;

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        //For- after login
        try {
            JSONObject credentialIS = getInternalStorage("credential.txt");
            JSONObject retrieve = credentialIS.getJSONObject("details");
            id = retrieve.getString("id");

            user_fullName.setText(retrieve.getString("fname") + " " + retrieve.getString("lname"));
            user_Email.setText(retrieve.getString("email"));
            Log.d("user", "info " + credentialIS.getString("details"));
            Log.d("creds", "Creds id: " + retrieve.getString("id") +" " + retrieve.getString("email"));
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }


//        new GoalsRequestAsynTask().execute("http://143.198.237.154:3001/api/getusergoal/" + id);
        GoalsRequestAsynTask readTask = new GoalsRequestAsynTask();
        readTask.execute("http://143.198.237.154:3001/api/getuserincome/" + id);
        if (isFileExists("profile.txt")){
            try{
                JSONObject profilegoals = getInternalStorage("profile.txt");
                Log.d("user", "info " + profilegoals.get("monthly_income"));

                // get results from readTask
                Log.d("info","info " + readTask);

                // add text to profile
                profile_monthlyIncome.setText("$ " + profilegoals.get("monthly_income"));
                profile_allSavings.setText("$ " + profilegoals.get("all_savings"));
                profile_expenses.setText("$ " + profilegoals.get("expenses"));
                profile_currentBalance.setText("$ " + profilegoals.get("current_balance"));
            }catch (JSONException e){
                throw new RuntimeException(e);
            }
        }

            Log.d("info","readTask: " + readTask);


    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public boolean isFileExists(String fileName) {
        File path = getActivity().getFilesDir();
        File file = new File(path, fileName);
        return file.exists();
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

    protected class GoalsRequestAsynTask extends AsyncTask<String, Void, String> {

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
                            Log.d("JSONObject", "monthly_income: " + monthly_income);
                            Log.d("JSONObject", "all_savings: " + all_savings);
                            Log.d("JSONObject", "expenses: " + expenses);
                            Log.d("JSONObject", "current_balance: " + current_balance);
                            profile.put("monthly_income", monthly_income);
                            profile.put("all_savings", all_savings);
                            profile.put("expenses", expenses);
                            profile.put("current_balance", current_balance);
                        }
                        Log.d("JSONArray", " " + profile);
                        writeToInternalStorage("profile.txt", profile.toString());
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

        public void writeToInternalStorage(String fileName, String content) {
            File path = getActivity().getFilesDir();

            try {
                FileOutputStream writer = new FileOutputStream(new File(path, fileName));
                writer.write(content.getBytes());
                Log.d("Internal Storage", "Profile: " + content);
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}


