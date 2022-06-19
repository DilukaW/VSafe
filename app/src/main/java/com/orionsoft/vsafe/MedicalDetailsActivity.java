package com.orionsoft.vsafe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.orionsoft.vsafe.model.Guardian;
import com.orionsoft.vsafe.model.Medical;
import com.orionsoft.vsafe.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    String medID = "";

    private Button btnMedAdd;

    ProgressDialog progressDialog;

    RequestQueue queue; // Volley RequestQueue
    StringRequest stringRequest; // Volley StringRequest
    JSONObject jsonObject;
    User user;

    ListView listView;
    List<Medical> medicalDetailsList; // The medicalDetailsList where we will store all the medical objects after parsing the JSON

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMedAdd:
                Intent intent = new Intent(this, AddMedicalActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            default:
                break;
        }
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_details);

        btnMedAdd = findViewById(R.id.btnMedAdd);

        queue = Volley.newRequestQueue(this); // Instantiate the RequestQueue
        user = SharedPrefManager.getInstance(this).getUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        // Instantiate the setOnClickListener(s) at runtime
        btnMedAdd.setOnClickListener(this);

        // Initializing listview and medicalDetailsList
        listView = (ListView) findViewById(R.id.listViewMedical);
        medicalDetailsList = new ArrayList<>();

        // Fetch medical details from the database
        getMedical(user.getNICNumber());

        progressDialog.show(); // Show ProgressDialog

        // Fetch and load data to the ListView
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();

                if (!(jsonObject == null)) {
                    loadMedicalDetailsList(); // Fetch and load data to the ListView
                }
            }
        }, 3000);

        // ListView Items LongPress Menu - OnItemLongClickListener
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idView = listView.findViewById(R.id.txtMedId);
                medID = idView.getText().toString();
                registerForContextMenu(listView);
                return false;
            }
        });
    }

//        -----------------------------------------------------------------------------------------------

    // When the BACK button is pressed, the activity on the stack is restarted
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

//        -----------------------------------------------------------------------------------------------

    // Get medical details
    private void getMedical(String nic) {

        stringRequest = new StringRequest(Request.Method.POST, URLs.getMedical,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            jsonObject = new JSONObject(response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nic", nic);

                return params;
            }
        };
        queue.add(stringRequest);
    }

//        -----------------------------------------------------------------------------------------------

    private void loadMedicalDetailsList() {
        try {
            // We have the array inside the JSON object
            // So here we are getting that JSON array
            JSONArray medicalArray = jsonObject.getJSONArray("Medical_Details");
            // Now looping through all the elements of the JSON array
            for (int i = 0; i < medicalArray.length(); i++) {
                // Getting the JSON object of the particular index inside the array
                JSONObject medObject = medicalArray.getJSONObject(i);
                // Creating a medical object and giving them the values from json object
                Medical medical = new Medical(medObject.getString("id"), medObject.getString("disease"), medObject.getString("time_period"), medObject.getString("under_treatments"));
                // Adding the medical to medicalDetailsList
                medicalDetailsList.add(medical);
            }
            // Creating custom adapter object
            MedicalListViewAdapter medicalListViewAdapter = new MedicalListViewAdapter(medicalDetailsList, getApplicationContext());
            // Adding the adapter to the ListView
            listView.setAdapter(medicalListViewAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // Menu header with title icon etc
//        menu.setHeaderTitle("Choose a color");
        // Menu items
        menu.add(0, v.getId(), 0, "Delete");
    }

    // Menu item select listener
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Delete") {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_exit)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMedical(medID);
                            progressDialog.show(); // Show ProgressDialog

                            // Fetch and load data to the ListView
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();

                                    onRestart();
                                }
                            }, 3000);
                        }

                    })
                    .setNegativeButton("No", null).show();
        }

        return true;
    }

//        -----------------------------------------------------------------------------------------------

    // Delete medical details
    private void deleteMedical(String id) {

        stringRequest = new StringRequest(Request.Method.POST, URLs.deleteMedical,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            jsonObject = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id", id);

                return params;
            }
        };
        queue.add(stringRequest);
    }
}