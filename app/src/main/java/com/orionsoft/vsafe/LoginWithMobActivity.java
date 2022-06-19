package com.orionsoft.vsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
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

public class LoginWithMobActivity extends AppCompatActivity implements View.OnClickListener {

    String verificationCode = null;
    String usrCheckMsg = "";
    int responseStatusCode = 0;

    private EditText edTxtMobNumber;
    private EditText edTxtMobVerify;

    private TextView txtMobOTPMsg;
    private TextView txtEmailLogin;
    private TextView txtAccntReg1;

    private Button btnMobNumLogin;

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
            case R.id.btnMobNumLogin:
                String btnText = btnMobNumLogin.getText().toString();
                String mobNumber = edTxtMobNumber.getText().toString();
                String mobVerify = edTxtMobVerify.getText().toString();
//  ----------------- Check whether the user is already exist and send the verification code --------------
                if (mobNumber.isEmpty()) {
                    edTxtMobNumber.setError("Please enter your mobile number!");
                } else if (mobNumber.length() < 10) {
                    edTxtMobNumber.setError("Please enter a valid mobile number!");
                } else if (!mobNumber.isEmpty() && btnText.equals("Next")) {
                    checkUser(mobNumber); // Check user
                    progressDialog.show();

                    if (responseStatusCode == 0) {
                        checkUser(mobNumber); // Check user
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            if (usrCheckMsg.equals("User found!")) {
                                sendSMS(mobNumber, verificationCode);
                                txtMobOTPMsg.setText("Enter the OTP sent to +94" + mobNumber.substring(1));
                                txtMobOTPMsg.setVisibility(View.VISIBLE);
                                edTxtMobVerify.setVisibility(View.VISIBLE);
                                edTxtMobVerify.requestFocus();
                                btnMobNumLogin.setText("Log In");
                            } else {
                                Toast.makeText(LoginWithMobActivity.this, "No user with this mobile number has been found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 3000);

                }
//  ----------------- Login --------------
                if (mobVerify.isEmpty() && btnText.equals("Log In")) {
                    edTxtMobVerify.setError("Please enter the verification code!");
                } else if (!mobNumber.isEmpty() && !mobVerify.isEmpty() && btnText.equals("Log In")){
                    if (mobVerify.equals(verificationCode)){
//                        Session management and redirection
                        // Storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        // Direct to the Dashboard activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    } else {
                        edTxtMobVerify.setError("Provided verification code is invalid!");
                        Toast.makeText(this, "Provided verification code is invalid!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.txtEmailLogin:
                Intent intent2 = new Intent(this, LoginWithEmailActivity.class);
                startActivity(intent2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.txtAccntReg1:
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
        setContentView(R.layout.activity_login_with_mob);

        // If the user is already logged in, this will redirect the user to the Dashboard
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, DashboardActivity.class));
        }

        txtMobOTPMsg = findViewById(R.id.txtMobOTPMsg);
        verificationCode = generateVerificationCode.generateCode();
        queue = Volley.newRequestQueue(this); // Instantiate the RequestQueue
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

//        -----------------------------------------------------------------------------------------------

        // Instantiate the setOnClickListener(s) at runtime
        txtEmailLogin = findViewById(R.id.txtEmailLogin);
        txtEmailLogin.setOnClickListener(this);

        txtAccntReg1 = findViewById(R.id.txtAccntReg1);
        txtAccntReg1.setOnClickListener(this);

        btnMobNumLogin = findViewById(R.id.btnMobNumLogin);
        btnMobNumLogin.setOnClickListener(this);

//        -----------------------------------------------------------------------------------------------

//        Remove focus from edTxtMobNumber when the editing is done
        edTxtMobNumber = findViewById(R.id.edTxtMobNumber);

        edTxtMobNumber.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    edTxtMobNumber.setFocusable(false);
                    edTxtMobNumber.setFocusableInTouchMode(true);
                    return true;
                } else {
                    return false;
                }
            }
        });

        //        Remove focus from edTxtMobVerify when the editing is done
        edTxtMobVerify = findViewById(R.id.edTxtMobVerify);

        edTxtMobVerify.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
                    edTxtMobVerify.setFocusable(false);
                    edTxtMobVerify.setFocusableInTouchMode(true);
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

    private void checkUser(String mobNumber) {

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
                params.put("mobNumber", mobNumber);

                return params;
            }
        };
        queue.add(stringRequest);
    }

//        -----------------------------------------------------------------------------------------------

    private void sendSMS(String mobNumber, String verificationCode) {

        // Request a string response from the provided URL
        stringRequest = new StringRequest(Request.Method.POST, URLs.sendSMS,
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
                params.put("to", mobNumber.substring(1));
                params.put("msg", "Your VSafe verification code is: " + verificationCode);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}