package com.orionsoft.vsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.orionsoft.vsafe.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginWithEmailActivity extends AppCompatActivity implements View.OnClickListener {

    String verificationCode = null;
    String usrCheckMsg = "";
    int responseStatusCode = 0;

    private EditText edTxtEmail;
    private EditText edTxtEmailVerify;

    private TextView txtEmailOTPMsg;
    private TextView txtMobNumLogin;
    private TextView txtAccntReg2;

    private Button btnEmailLogin;

    ProgressDialog progressDialog;

    GenerateVerificationCode generateVerificationCode = new GenerateVerificationCode();
    RequestQueue queue; // Volley RequestQueue
    StringRequest stringRequest; // Volley StringRequest
    User user;


//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEmailLogin:
                String btnText = btnEmailLogin.getText().toString();
                String emailAddress = edTxtEmail.getText().toString();
                String emailVerify = edTxtEmailVerify.getText().toString();
//  ----------------- Check whether the user is already exist and send the verification code --------------
                if (emailAddress.isEmpty()) {
                    edTxtEmail.setError("Please enter your email address!");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    edTxtEmail.setError("Please enter a valid email address!");
                } else if (!emailAddress.isEmpty() && btnText.equals("Next")) {
                    checkUser(emailAddress); // Check user
                    progressDialog.show();

                    if (responseStatusCode == 0) {
                        checkUser(emailAddress); // Check user
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            if (usrCheckMsg.equals("User found!")) {
                                sendEmail(emailAddress, user.getFirstName(), user.getLastName(), verificationCode);
                                txtEmailOTPMsg.setText("Enter the verification code sent to " + emailAddress);
                                txtEmailOTPMsg.setVisibility(View.VISIBLE);
                                edTxtEmailVerify.setVisibility(View.VISIBLE);
                                edTxtEmailVerify.requestFocus();
                                btnEmailLogin.setText("Log In");
                            } else {
                                Toast.makeText(LoginWithEmailActivity.this, "No user with this email address has been found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 3000);

                }
//  ----------------- Login --------------
                if (emailVerify.isEmpty() && btnText.equals("Log In")) {
                    edTxtEmailVerify.setError("Please enter the verification code!");
                } else if (!emailAddress.isEmpty() && !emailVerify.isEmpty() && btnText.equals("Log In")){
                    if (emailVerify.equals(verificationCode)){
//                        Session management and redirection
                        // Storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        // Direct to the Dashboard activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    } else {
                        edTxtEmailVerify.setError("Provided verification code is invalid!");
                        Toast.makeText(this, "Provided verification code is invalid!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.txtMobNumLogin:
                Intent intent2 = new Intent(this, LoginWithMobActivity.class);
                startActivity(intent2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.txtAccntReg2:
                Intent intent3 = new Intent(this, RegistrationActivity.class);
                startActivity(intent3);
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
        setContentView(R.layout.activity_login_with_email);

        // If the user is already logged in, this will redirect the user to the Dashboard
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, DashboardActivity.class));
        }

        txtEmailOTPMsg = findViewById(R.id.txtEmailOTPMsg);
        verificationCode = generateVerificationCode.generateCode();
        queue = Volley.newRequestQueue(this); // Instantiate the RequestQueue
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

//        -----------------------------------------------------------------------------------------------

        // Instantiate the setOnClickListener(s) at runtime
        txtMobNumLogin = findViewById(R.id.txtMobNumLogin);
        txtMobNumLogin.setOnClickListener(this);

        txtAccntReg2 = findViewById(R.id.txtAccntReg2);
        txtAccntReg2.setOnClickListener(this);

        btnEmailLogin = findViewById(R.id.btnEmailLogin);
        btnEmailLogin.setOnClickListener(this);

//        -----------------------------------------------------------------------------------------------

//        Remove focus from edTxtEmail when the editing is done
        edTxtEmail = findViewById(R.id.edTxtEmail);

        edTxtEmail.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    edTxtEmail.setFocusable(false);
                    edTxtEmail.setFocusableInTouchMode(true);
                    return true;
                } else {
                    return false;
                }
            }
        });

        //        Remove focus from edTxtEmailVerify when the editing is done
        edTxtEmailVerify = findViewById(R.id.edTxtEmailVerify);

        edTxtEmailVerify.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    edTxtEmailVerify.setFocusable(false);
                    edTxtEmailVerify.setFocusableInTouchMode(true);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finishAffinity(); // Removes the connection of the existing activity to its stack
        finish(); // The method onDestroy() is executed & exit the application
    }

//        -----------------------------------------------------------------------------------------------

    private void checkUser(String emailAddress) {

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

                            // Creating a new user object
                            if (jsonObject.getString("message").equals("User found!")) {
                                user = new User(
                                        jsonObject.getJSONObject("user").getString("nic"),
                                        jsonObject.getJSONObject("user").getString("first_name"),
                                        jsonObject.getJSONObject("user").getString("last_name"),
                                        jsonObject.getJSONObject("user").getString("mob_number"),
                                        jsonObject.getJSONObject("user").getString("email")
                                );
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
                params.put("email", emailAddress);

                return params;
            }
        };
        queue.add(stringRequest);
    }

//        -----------------------------------------------------------------------------------------------

    private void sendEmail(String emailAddress, String fName, String lName, String verificationCode) {

        // Request a string response from the provided URL
        stringRequest = new StringRequest(Request.Method.POST, URLs.sendEmail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("to", emailAddress);
                params.put("name", fName + " " + lName);
                params.put("subject", "VSafe Verification Code");
                params.put("content", "Hi " + fName + ", Please enter this verification code to access your Vsafe account: " + verificationCode);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}