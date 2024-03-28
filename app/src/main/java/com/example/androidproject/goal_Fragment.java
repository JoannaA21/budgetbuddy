package com.example.androidproject;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link goal_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class goal_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText goal_amountGoal, goal_type_other;
    Spinner spinner_goal_type;

    public goal_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment goal_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static goal_Fragment newInstance(String param1, String param2) {
        goal_Fragment fragment = new goal_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        goal_amountGoal = view.findViewById(R.id.goal_amountGoal);
        goal_type_other = view.findViewById(R.id.goal_type_other);
        spinner_goal_type = view.findViewById(R.id.spinner);

        // Create a list of items for the Spinner
        List<String> items = new ArrayList<>();
        items.add("Select Goal Type ");
        items.add("Rent");
        items.add("Car");
        items.add("Grocery");
        items.add("Utility");
        items.add("Mortgage");
        items.add("Phone");
        items.add("Insurance");
        items.add("Other");

        // Create an ArrayAdapter using the string array and a default Spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner_goal_type.setAdapter(adapter);

        spinner_goal_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, android.view.View selectedItemView, int position, long id) {
                // Display a Toast message showing the selected item
                String selectedItem = (String) parentView.getItemAtPosition(position);
                Toast.makeText(getActivity(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goal_, container, false);
    }

    protected class GoalRequestAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            String response = null;


//            try {
//                //Retrieve user input
//                String amountGoal = goal_amountGoal.getText().toString();
//                String typeOther = goal_type_other.getText().toString();
//
//
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//                //response = "Exception: " + e.getMessage();
//            }
//        }
//        return response;
            return response;
        }

        protected void onPostExecute(String result) {

        }
    }
}