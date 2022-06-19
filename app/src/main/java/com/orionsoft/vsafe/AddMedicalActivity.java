package com.orionsoft.vsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddMedicalActivity extends AppCompatActivity implements View.OnClickListener{

    String usrCheckMsg = "";

    private EditText edTxtAddMedDis;
    private EditText edTxtAddMedTime;

    private RadioGroup raGroupAddMedTreat;
    private RadioButton raBtnAddMedTreat;
    private RadioButton raBtnAddMedTreatYes;
    private RadioButton raBtnAddMedTreatNo;

    private Button btnAddMedSubmit;

    final Calendar myCalendar= Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    ProgressDialog progressDialog;

    RequestQueue queue; // Volley RequestQueue
    StringRequest stringRequest; // Volley StringRequest
    Medical medical;
    User user;

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edTxtAddMedTime:
                showDatePickerDialog();
                break;
            case R.id.btnAddMedSubmit:
                if (validateFields() == true) {
                    fetchData(); // Fetch user inputs
                    addMedical(); // Add medical details
                    progressDialog.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            if (usrCheckMsg.equals("Successfully Added!")) {
                                Toast.makeText(AddMedicalActivity.this, "Successfully Added!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddMedicalActivity.this, "Something unexpected happened!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 3000);

                } else {
                    Toast.makeText(this, "Whoops! There were some problems with you inputs!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical);

        queue = Volley.newRequestQueue(this); // Instantiate the RequestQueue
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");

        user = SharedPrefManager.getInstance(this).getUser();

        edTxtAddMedDis = findViewById(R.id.edTxtAddMedDis);
        edTxtAddMedTime = findViewById(R.id.edTxtAddMedTime);

        raGroupAddMedTreat = findViewById(R.id.raGroupAddMedTreat);
        raBtnAddMedTreatYes = findViewById(R.id.raBtnAddMedTreatYes);
        raBtnAddMedTreatNo = findViewById(R.id.raBtnAddMedTreatNo);

        btnAddMedSubmit = findViewById(R.id.btnAddMedSubmit);


        // Instantiate the setOnClickListener(s) at runtime
        btnAddMedSubmit.setOnClickListener(this);
        edTxtAddMedTime.setOnClickListener(this);
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

//        -----------------------------------------------------------------------------------------------

    //    View DatePickerDialog
    private void showDatePickerDialog(){
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                setDate();
            }
        };
        new DatePickerDialog(AddMedicalActivity.this,R.style.DatePickerDialog,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    //    Update editText field with date of birth
    private void setDate(){
        String myFormat="yyyy/MM/dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        edTxtAddMedTime.setText(dateFormat.format(myCalendar.getTime()));
    }

//        -----------------------------------------------------------------------------------------------

    // Validate fields
    private boolean validateFields() {
        if (TextUtils.isEmpty(edTxtAddMedDis.getText().toString())) {
            edTxtAddMedDis.setError("Field cannot be empty!");
        }

        if (TextUtils.isEmpty(edTxtAddMedTime.getText().toString())) {
            edTxtAddMedTime.setError("Field cannot be empty!");
        } else {
            edTxtAddMedTime.setError(null);
        }

        if (raGroupAddMedTreat.getCheckedRadioButtonId() == -1) {
            raBtnAddMedTreatNo.setError("Select item!");
        } else {
            raBtnAddMedTreatNo.setError(null);
        }

        // ---------------------------------------------------------------

        if (edTxtAddMedDis.getError() == null && edTxtAddMedTime.getError() == null && raBtnAddMedTreatNo.getError() == null) {
            return true;
        } else {
            return false;
        }
    }

//        -----------------------------------------------------------------------------------------------

    // Get data from user inputs
    private void fetchData() {
        // Getting value from the selected RadioButton
        raBtnAddMedTreat = findViewById(raGroupAddMedTreat.getCheckedRadioButtonId());

        medical = new Medical(
                edTxtAddMedDis.getText().toString(),
                edTxtAddMedTime.getText().toString(),
                raBtnAddMedTreat.getText().toString()
        );
    }

//        -----------------------------------------------------------------------------------------------

    // User Update
    private void addMedical() {
        stringRequest = new StringRequest(Request.Method.POST, URLs.addMedical,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            usrCheckMsg = jsonObject.getString("message");

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
                params.put("nic", user.getNICNumber());
                params.put("disease", medical.getDisease());
                params.put("timePeriod", medical.getTimePeriod());
                params.put("underTreat", medical.getUnderTreat());

                return params;
            }
        };
        queue.add(stringRequest);
    }
}