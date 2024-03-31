package com.example.androidproject;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link expense_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class expense_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    EditText input_expenseType, input_cost;
    TextView tv_currentIncome, tv_currentBalance;
    Button addButton;

    public expense_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment expense_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static expense_Fragment newInstance(String param1, String param2) {
        expense_Fragment fragment = new expense_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         input_expenseType = view.findViewById(R.id.input_expenseType);
         input_cost = view.findViewById(R.id.input_cost);
         tv_currentIncome = view.findViewById(R.id.tv_currentIncome);
         tv_currentBalance = view.findViewById(R.id.tv_currentBalance);
         addButton = view.findViewById(R.id.btn_addExpense);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation()) {
                    // Execute AsyncTask to send registration request
                    AddExpenseRequestAsyncTask readTask = new AddExpenseRequestAsyncTask();
                    readTask.execute("http://143.198.237.154:3001/api/createexpense");

                    input_expenseType.setText("");
                    input_cost.setText("");
                }
            }
        });

        //Access current_amount and current_income
        if (isFileExists("profilegoals.txt")) {

            try{
                JSONObject profilegoals = getInternalStorage("profilegoals.txt");
                tv_currentIncome.setText("$ " + profilegoals.get("monthly_income"));
                tv_currentBalance.setText("$ " + profilegoals.get("current_balance"));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean validation() {
        String expense_type = input_expenseType.getText().toString().trim();
        String cost = input_cost.getText().toString().trim();

        if (TextUtils.isEmpty(expense_type)) {
            input_expenseType.setError("The expense type must be filled out");
            return false;
        }

        if (TextUtils.isEmpty(cost)) {
            input_cost.setError("The cost must be filled out");
            return false;
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_, container, false);
    }

    protected class AddExpenseRequestAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            HttpURLConnection urlConnection = null;
            String response = null;

            try{
                //access id of user from credential.txt in the internal storage
                JSONObject credentialIS = getInternalStorage("credential.txt");
                JSONObject retrieve = credentialIS.getJSONObject("details");
                String id = retrieve.getString("id");

                //Retrieve user input
                String expense_type = input_expenseType.getText().toString().trim();
                String cost = input_cost.getText().toString().trim();

                //JSON object for user input
                JSONObject expenseInfo = new JSONObject();

                expenseInfo.put("expense_type", expense_type);
                expenseInfo.put("cost", cost);
                expenseInfo.put("user_id", id);

                //Create connection and send request
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true); // Allow writing data to the connection
                connection.setRequestProperty("Content-Type", "application/json");

                String jsonString = expenseInfo.toString(); //convert to String

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(jsonString);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuilder responseBuilder = new StringBuilder();

                    String line;

                    while ((line = in.readLine()) != null) {
                        responseBuilder.append(line);
                    }

                    in.close();

                    response = responseBuilder.toString();

                } else {
                    response = "POST request failed with response code: " + responseCode;
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
                response = "Exception: " + e.getMessage();
            }

            return response;
        }


        protected void onPostExecute(String result) {

            if(result != null) {
                Log.d("RegisterRequestAsyncTask", "Response: Successful " + result);
            }else {
                Log.d("RegisterRequestAsyncTask", "Response: Unsuccessful " + result);
            }
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

    public boolean isFileExists(String fileName) {
        File path = getActivity().getFilesDir();
        File file = new File(path, fileName);
        return file.exists();
    }
}



//*********************** Current Balance should change when user adds a new expense. FIX!!!