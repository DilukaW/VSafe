package com.orionsoft.vsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.orionsoft.vsafe.model.Guardian;
import com.orionsoft.vsafe.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    String usrCheckMsg = "";
    int responseStatusCode = 0;
    String tempNIC, tempMobNum, tempEmail;

    private EditText edTxtRegNICNo;
    private EditText edTxtRegFName;
    private EditText edTxtRegLName;
    private EditText edTxtRegDOB;
    private EditText edTxtRegAddress;
    private EditText edTxtRegMobNum;
    private EditText edTxtRegEmail;
    private EditText edTxtRegGNICNo;
    private EditText edTxtRegGName;
    private EditText edTxtRegGAddress;
    private EditText edTxtRegGConNum;

    private RadioGroup raGroupRegGender;
    private RadioButton raBtnGender;
    private RadioButton raBtnGenderF;

    private Spinner spnRegBloodGrp;
    private Spinner spnRegRelationship;

    private TextView txtAccntLogin;

    private Button btnRegister;

    final Calendar myCalendar= Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    ProgressDialog progressDialog;

    RequestQueue queue; // Volley RequestQueue
    StringRequest stringRequest; // Volley StringRequest
    User user;
    Guardian guardian;

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                if (validateFields() == true) {
                    fetchData(); // Fetch user inputs
                    // Check whether the user is already exist
                    checkUser(user.getNICNumber(), user.getMobNumber(), user.getEmailAddress());
                    progressDialog.show();

                    if (responseStatusCode == 0) {
                        checkUser(user.getNICNumber(), user.getMobNumber(), user.getEmailAddress()); // Check user
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            responseStatusCode = 0;
                            if (usrCheckMsg.equals("User not found!")) {
                                registerUser(); // User Registration
                                progressDialog.show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        progressDialog.dismiss();
                                        if (usrCheckMsg.equals("Registration Successful!")) {
                                            Toast.makeText(RegistrationActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                            // Session management and redirection
                                            // Storing the user in shared preferences
                                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                            // Direct to the Dashboard activity
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                        } else {
                                            Toast.makeText(RegistrationActivity.this, "Something unexpected happened!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, 3000);

                            } else {
                                if (user.getNICNumber().equals(tempNIC)) {
                                    edTxtRegNICNo.setError("This user is already exists!");
                                    edTxtRegNICNo.requestFocus();
                                    Toast.makeText(RegistrationActivity.this, "This user is already exists!", Toast.LENGTH_SHORT).show();
                                }
                                if (user.getMobNumber().equals(tempMobNum)) {
                                    edTxtRegMobNum.setError("This number is already in use!");
                                    edTxtRegMobNum.requestFocus();
                                    Toast.makeText(RegistrationActivity.this, "This number is already in use!", Toast.LENGTH_SHORT).show();
                                }
                                if (user.getEmailAddress().equals(tempEmail)) {
                                    edTxtRegEmail.setError("This email is already in use!");
                                    edTxtRegEmail.requestFocus();
                                    Toast.makeText(RegistrationActivity.this, "This email is already in use!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }, 3000);
                } else {
                    Toast.makeText(this, "Whoops! There were some problems with you inputs!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edTxtRegDOB:
                showDatePickerDialog();
                break;
            case R.id.txtAccntLogin:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            default:
                break;
        }
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // If the user is already logged in, this will redirect the user to the Dashboard
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, DashboardActivity.class));
        }

        queue = Volley.newRequestQueue(this); // Instantiate the RequestQueue
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        edTxtRegNICNo = findViewById(R.id.edTxtRegNICNo);
        edTxtRegFName = findViewById(R.id.edTxtRegFName);
        edTxtRegLName = findViewById(R.id.edTxtRegLName);
        edTxtRegAddress = findViewById(R.id.edTxtRegAddress);
        edTxtRegMobNum = findViewById(R.id.edTxtRegMobNum);
        edTxtRegEmail = findViewById(R.id.edTxtRegEmail);
        edTxtRegGNICNo = findViewById(R.id.edTxtRegGNICNo);
        edTxtRegGName = findViewById(R.id.edTxtRegGName);
        edTxtRegGAddress = findViewById(R.id.edTxtRegGAddress);
        edTxtRegGConNum = findViewById(R.id.edTxtRegGConNum);

        raGroupRegGender = findViewById(R.id.raGroupRegGender);
        raBtnGenderF = findViewById(R.id.raBtnRegFemale);

        spnRegBloodGrp = findViewById(R.id.spnRegBloodGrp);
        spnRegRelationship = findViewById(R.id.spnRegRelationship);

//        -----------------------------------------------------------------------------------------------

        // Instantiate the setOnClickListener(s) at runtime
        edTxtRegDOB = findViewById(R.id.edTxtRegDOB);
        edTxtRegDOB.setOnClickListener(this);

        txtAccntLogin = findViewById(R.id.txtAccntLogin);
        txtAccntLogin.setOnClickListener(this);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
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
        new DatePickerDialog(RegistrationActivity.this,R.style.DatePickerDialog,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

//    Update editText field with date of birth
    private void setDate(){
        String myFormat="yyyy/MM/dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        edTxtRegDOB.setText(dateFormat.format(myCalendar.getTime()));
    }

//        -----------------------------------------------------------------------------------------------

    // Get data from user inputs
    private void fetchData() {
        // Getting value from the selected RadioButton
        raBtnGender = findViewById(raGroupRegGender.getCheckedRadioButtonId());

        user = new User(
                edTxtRegNICNo.getText().toString(),
                edTxtRegFName.getText().toString(),
                edTxtRegLName.getText().toString(),
                raBtnGender.getText().toString(),
                edTxtRegDOB.getText().toString(),
                edTxtRegAddress.getText().toString(),
                edTxtRegMobNum.getText().toString(),
                edTxtRegEmail.getText().toString(),
                spnRegBloodGrp.getSelectedItem().toString()
        );

        guardian = new Guardian(
                edTxtRegGNICNo.getText().toString(),
                edTxtRegGName.getText().toString(),
                edTxtRegGAddress.getText().toString(),
                edTxtRegGConNum.getText().toString(),
                spnRegRelationship.getSelectedItem().toString()
        );
    }

//        -----------------------------------------------------------------------------------------------

    // Validate fields
    private boolean validateFields() {
        if (TextUtils.isEmpty(edTxtRegNICNo.getText().toString())) {
            edTxtRegNICNo.setError("Field cannot be empty!");
        } else if (edTxtRegNICNo.length() < 10) {
            edTxtRegNICNo.setError("Invalid NIC number!");
        }

        if (TextUtils.isEmpty(edTxtRegFName.getText().toString())) {
            edTxtRegFName.setError("Field cannot be empty!");
        }

        if (TextUtils.isEmpty(edTxtRegLName.getText().toString())) {
            edTxtRegLName.setError("Field cannot be empty!");
        }

        if (raGroupRegGender.getCheckedRadioButtonId() == -1) {
            raBtnGenderF.setError("Select item!");
        } else {
            raBtnGenderF.setError(null);
        }

        if (TextUtils.isEmpty(edTxtRegDOB.getText().toString())) {
            edTxtRegDOB.setError("Field cannot be empty!");
        } else {
            edTxtRegDOB.setError(null);
        }

        if (TextUtils.isEmpty(edTxtRegAddress.getText().toString())) {
            edTxtRegAddress.setError("Field cannot be empty!");
        }

        if (TextUtils.isEmpty(edTxtRegMobNum.getText().toString())) {
            edTxtRegMobNum.setError("Field cannot be empty!");
        } else if (edTxtRegMobNum.length() < 10) {
            edTxtRegMobNum.setError("Invalid mobile number!");
        }

        if (TextUtils.isEmpty(edTxtRegEmail.getText().toString())) {
            edTxtRegEmail.setError("Field cannot be empty!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edTxtRegEmail.getText().toString()).matches()) {
            edTxtRegEmail.setError("Invalid email address!");
        }

        if (TextUtils.isEmpty(edTxtRegGNICNo.getText().toString())) {
            edTxtRegGNICNo.setError("Field cannot be empty!");
        } else if (edTxtRegGNICNo.length() < 10) {
            edTxtRegGNICNo.setError("Invalid NIC number!");
        }

        if (TextUtils.isEmpty(edTxtRegGName.getText().toString())) {
            edTxtRegGName.setError("Field cannot be empty!");
        }

        if (TextUtils.isEmpty(edTxtRegGAddress.getText().toString())) {
            edTxtRegGAddress.setError("Field cannot be empty!");
        }

        if (TextUtils.isEmpty(edTxtRegGConNum.getText().toString())) {
            edTxtRegGConNum.setError("Field cannot be empty!");
        } else if (edTxtRegGConNum.length() < 10) {
            edTxtRegGConNum.setError("Invalid mobile number!");
        }

        // ---------------------------------------------------------------

        if (edTxtRegNICNo.getError() == null && edTxtRegFName.getError() == null && edTxtRegLName.getError() == null && raBtnGenderF.getError() == null && edTxtRegDOB.getError() == null && edTxtRegAddress.getError() == null && edTxtRegMobNum.getError() == null && edTxtRegEmail.getError() == null && edTxtRegGNICNo.getError() == null && edTxtRegGName.getError() == null && edTxtRegGAddress.getError() == null && edTxtRegGConNum.getError() == null) {
            return true;
        } else {
            return false;
        }
    }

//        -----------------------------------------------------------------------------------------------

    // Check whether the user is already exist
    private void checkUser(String nic, String mobNumber, String email) {

        stringRequest = new StringRequest(Request.Method.POST, URLs.usrCheck,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            usrCheckMsg = jsonObject.getString("message");
                            responseStatusCode = 1;

                            if (jsonObject.getString("message").equals("User found!")) {
                                tempNIC = jsonObject.getJSONObject("user").getString("nic");
                                tempMobNum = jsonObject.getJSONObject("user").getString("mob_number");
                                tempEmail = jsonObject.getJSONObject("user").getString("email");
                            }

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
                params.put("mobNumber", mobNumber);
                params.put("email", email);

                return params;
            }
        };
        queue.add(stringRequest);
    }

//        -----------------------------------------------------------------------------------------------

    // User Registration
    private void registerUser() {
        stringRequest = new StringRequest(Request.Method.POST, URLs.registerUser,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            usrCheckMsg = jsonObject.getString("message");
                            responseStatusCode = 1;

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
                params.put("firstName", user.getFirstName());
                params.put("lastName", user.getLastName());
                params.put("gender", user.getGender());
                params.put("dob", user.getDob());
                params.put("address", user.getAddress());
                params.put("mobNumber", user.getMobNumber());
                params.put("email", user.getEmailAddress());
                params.put("bloodGroup", user.getBloodGroup());
                params.put("gNIC", guardian.getNICNumber());
                params.put("gName", guardian.getName());
                params.put("gAddress", guardian.getAddress());
                params.put("gConNumber", guardian.getConNumber());
                params.put("relationship", guardian.getRelationship());

                return params;
            }
        };
        queue.add(stringRequest);
    }
}