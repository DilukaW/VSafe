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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    String usrCheckMsg = "";

    private EditText edTxtProfNICNo;
    private EditText edTxtProfFName;
    private EditText edTxtProfLName;
    private EditText edTxtProfDOB;
    private EditText edTxtProfAddress;
    private EditText edTxtProfMobNum;
    private EditText edTxtProfEmail;
    private EditText edTxtProfGNICNo;
    private EditText edTxtProfGName;
    private EditText edTxtProfGAddress;
    private EditText edTxtProfGConNum;

    private RadioGroup raGroupProfGender;
    private RadioButton raBtnGender;
    private RadioButton raBtnProfMale;
    private RadioButton raBtnProfFemale;

    private Spinner spnProfBloodGrp;
    private Spinner spnProfRelationship;

    private ImageView imgProfAvatar;

    private Button btnProfUpdate;

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
            case R.id.edTxtProfDOB:
                showDatePickerDialog();
                break;
            case R.id.btnProfUpdate:
                if (validateFields() == true) {
                    fetchData(); // Fetch user inputs
                    updateUser(); // Update user
                    progressDialog.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            if (usrCheckMsg.equals("User Updated Successfully!")) {
                                Toast.makeText(EditProfileActivity.this, "User Updated Successfully!", Toast.LENGTH_SHORT).show();
                                // Session management and redirection
                                // Storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                // Direct to the Dashboard activity
                                finish();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Something unexpected happened!", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_edit_profile);

        queue = Volley.newRequestQueue(this); // Instantiate the RequestQueue
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");

        edTxtProfNICNo = findViewById(R.id.edTxtProfNICNo);
        edTxtProfFName = findViewById(R.id.edTxtProfFName);
        edTxtProfLName = findViewById(R.id.edTxtProfLName);
        edTxtProfDOB = findViewById(R.id.edTxtProfDOB);
        edTxtProfAddress = findViewById(R.id.edTxtProfAddress);
        edTxtProfMobNum = findViewById(R.id.edTxtProfMobNum);
        edTxtProfEmail = findViewById(R.id.edTxtProfEmail);
        edTxtProfGNICNo = findViewById(R.id.edTxtProfGNICNo);
        edTxtProfGName = findViewById(R.id.edTxtProfGName);
        edTxtProfGAddress = findViewById(R.id.edTxtProfGAddress);
        edTxtProfGConNum = findViewById(R.id.edTxtProfGConNum);

        raGroupProfGender = findViewById(R.id.raGroupProfGender);
        raBtnProfMale = findViewById(R.id.raBtnProfMale);
        raBtnProfFemale = findViewById(R.id.raBtnProfFemale);

        spnProfBloodGrp = findViewById(R.id.spnProfBloodGrp);
        spnProfRelationship = findViewById(R.id.spnProfRelationship);

        imgProfAvatar = findViewById(R.id.imgProfAvatar2);

        btnProfUpdate = findViewById(R.id.btnProfUpdate);

        // Retrieve objects from the previous activity
        user = (User) getIntent().getSerializableExtra("userObj");
        guardian = (Guardian) getIntent().getSerializableExtra("guardObj");

        setDataToFields(); // Add data to EditText fields when loading the activity


        // Instantiate the setOnClickListener(s) at runtime
        edTxtProfDOB.setOnClickListener(this);
        btnProfUpdate.setOnClickListener(this);

    }

//        -----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

//        -----------------------------------------------------------------------------------------------

    // Add data to EditText fields when loading the activity
    private void setDataToFields() {
        edTxtProfNICNo.setText(user.getNICNumber());
        edTxtProfFName.setText(user.getFirstName());
        edTxtProfLName.setText(user.getLastName());
        edTxtProfDOB.setText(user.getDob());
        edTxtProfAddress.setText(user.getAddress());
        edTxtProfMobNum.setText("0" + user.getMobNumber());
        edTxtProfEmail.setText(user.getEmailAddress());
        edTxtProfGNICNo.setText(guardian.getNICNumber());
        edTxtProfGName.setText(guardian.getName());
        edTxtProfGAddress.setText(guardian.getAddress());
        edTxtProfGConNum.setText("0" + guardian.getConNumber());

        if (user.getGender().equals("Male")) {
            raBtnProfMale.setChecked(true);
        } else {
            raBtnProfFemale.setChecked(true);
        }

        if (user.getGender().equals("Female")) {
            imgProfAvatar.setImageResource(R.drawable.avatar_woman);
        }

        for (int i = 0; i < spnProfBloodGrp.getCount(); i++) {
            if (spnProfBloodGrp.getItemAtPosition(i).equals(user.getBloodGroup())) {
                spnProfBloodGrp.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < spnProfRelationship.getCount(); i++) {
            if (spnProfRelationship.getItemAtPosition(i).equals(guardian.getRelationship())) {
                spnProfRelationship.setSelection(i);
                break;
            }
        }
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
        new DatePickerDialog(EditProfileActivity.this,R.style.DatePickerDialog,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    //    Update editText field with date of birth
    private void setDate(){
        String myFormat="yyyy/MM/dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        edTxtProfDOB.setText(dateFormat.format(myCalendar.getTime()));
    }

//        -----------------------------------------------------------------------------------------------

    // Validate fields
    private boolean validateFields() {
        if (TextUtils.isEmpty(edTxtProfNICNo.getText().toString())) {
            edTxtProfNICNo.setError("Field cannot be empty!");
        } else if (edTxtProfNICNo.length() < 10) {
            edTxtProfNICNo.setError("Invalid NIC number!");
        }

        if (TextUtils.isEmpty(edTxtProfFName.getText().toString())) {
            edTxtProfFName.setError("Field cannot be empty!");
        }

        if (TextUtils.isEmpty(edTxtProfLName.getText().toString())) {
            edTxtProfLName.setError("Field cannot be empty!");
        }

        if (raGroupProfGender.getCheckedRadioButtonId() == -1) {
            raBtnProfFemale.setError("Select item!");
        } else {
            raBtnProfFemale.setError(null);
        }

        if (TextUtils.isEmpty(edTxtProfDOB.getText().toString())) {
            edTxtProfDOB.setError("Field cannot be empty!");
        } else {
            edTxtProfDOB.setError(null);
        }

        if (TextUtils.isEmpty(edTxtProfAddress.getText().toString())) {
            edTxtProfAddress.setError("Field cannot be empty!");
        }

        if (TextUtils.isEmpty(edTxtProfMobNum.getText().toString())) {
            edTxtProfMobNum.setError("Field cannot be empty!");
        } else if (edTxtProfMobNum.length() < 10) {
            edTxtProfMobNum.setError("Invalid mobile number!");
        }

        if (TextUtils.isEmpty(edTxtProfEmail.getText().toString())) {
            edTxtProfEmail.setError("Field cannot be empty!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edTxtProfEmail.getText().toString()).matches()) {
            edTxtProfEmail.setError("Invalid email address!");
        }

        if (TextUtils.isEmpty(edTxtProfGNICNo.getText().toString())) {
            edTxtProfGNICNo.setError("Field cannot be empty!");
        } else if (edTxtProfGNICNo.length() < 10) {
            edTxtProfGNICNo.setError("Invalid NIC number!");
        }

        if (TextUtils.isEmpty(edTxtProfGName.getText().toString())) {
            edTxtProfGName.setError("Field cannot be empty!");
        }

        if (TextUtils.isEmpty(edTxtProfGAddress.getText().toString())) {
            edTxtProfGAddress.setError("Field cannot be empty!");
        }

        if (TextUtils.isEmpty(edTxtProfGConNum.getText().toString())) {
            edTxtProfGConNum.setError("Field cannot be empty!");
        } else if (edTxtProfGConNum.length() < 10) {
            edTxtProfGConNum.setError("Invalid mobile number!");
        }

        // ---------------------------------------------------------------

        if (edTxtProfNICNo.getError() == null && edTxtProfFName.getError() == null && edTxtProfLName.getError() == null && raBtnProfFemale.getError() == null && edTxtProfDOB.getError() == null && edTxtProfAddress.getError() == null && edTxtProfMobNum.getError() == null && edTxtProfEmail.getError() == null && edTxtProfGNICNo.getError() == null && edTxtProfGName.getError() == null && edTxtProfGAddress.getError() == null && edTxtProfGConNum.getError() == null) {
            return true;
        } else {
            return false;
        }
    }

//        -----------------------------------------------------------------------------------------------

    // Get data from user inputs
    private void fetchData() {
        // Getting value from the selected RadioButton
        raBtnGender = findViewById(raGroupProfGender.getCheckedRadioButtonId());

        user = new User(
                edTxtProfNICNo.getText().toString(),
                edTxtProfFName.getText().toString(),
                edTxtProfLName.getText().toString(),
                raBtnGender.getText().toString(),
                edTxtProfDOB.getText().toString(),
                edTxtProfAddress.getText().toString(),
                edTxtProfMobNum.getText().toString(),
                edTxtProfEmail.getText().toString(),
                spnProfBloodGrp.getSelectedItem().toString()
        );

        guardian = new Guardian(
                edTxtProfGNICNo.getText().toString(),
                edTxtProfGName.getText().toString(),
                edTxtProfGAddress.getText().toString(),
                edTxtProfGConNum.getText().toString(),
                spnProfRelationship.getSelectedItem().toString()
        );
    }

//        -----------------------------------------------------------------------------------------------

    // User Update
    private void updateUser() {
        stringRequest = new StringRequest(Request.Method.POST, URLs.updateUser,
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