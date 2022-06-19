package com.orionsoft.vsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    String usrCheckMsg = "";

    private TextView txtProfNic;
    private TextView txtProfName;
    private TextView txtProfGender;
    private TextView txtProfDOB;
    private TextView txtProfAddress;
    private TextView txtProfMobNumber;
    private TextView txtProfEmail;
    private TextView txtProfBloodGrp;
    private TextView txtProfGNic;
    private TextView txtProfGName;
    private TextView txtProfGAddress;
    private TextView txtProfGConNum;
    private TextView txtProfRelationship;

    private ImageView imgProfAvatar;

    private Button btnProfEdit;

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
            case R.id.btnProfEdit:
                Intent intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra("userObj", user); // Pass User object from this to EditProfile Activity
                intent.putExtra("guardObj", guardian); // Pass Guardian object from this to EditProfile Activity
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
        setContentView(R.layout.activity_profile);

        txtProfNic = findViewById(R.id.txtProfNic);
        txtProfName = findViewById(R.id.txtProfName);
        txtProfGender = findViewById(R.id.txtProfGender);
        txtProfDOB = findViewById(R.id.txtProfDOB);
        txtProfAddress = findViewById(R.id.txtProfAddress);
        txtProfMobNumber = findViewById(R.id.txtProfMobNum);
        txtProfEmail = findViewById(R.id.txtProfEmail);
        txtProfBloodGrp = findViewById(R.id.txtProfBloodGrp);
        txtProfGNic = findViewById(R.id.txtProfGNic);
        txtProfGName = findViewById(R.id.txtProfGName);
        txtProfGAddress = findViewById(R.id.txtProfGAddress);
        txtProfGConNum = findViewById(R.id.txtProfGConNum);
        txtProfRelationship = findViewById(R.id.txtProfRelationship);

        imgProfAvatar = findViewById(R.id.imgProfAvatar);

        btnProfEdit = findViewById(R.id.btnProfEdit);


        queue = Volley.newRequestQueue(this); // Instantiate the RequestQueue

        user = SharedPrefManager.getInstance(this).getUser();
        fetchUser(user.getNICNumber());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        // Instantiate the setOnClickListener(s) at runtime
        btnProfEdit.setOnClickListener(this);

        progressDialog.show(); // Show ProgressDialog

        // Update fields with fetched data
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();

                if (usrCheckMsg.equals("Request Successfully Completed!")) {
                    setDataToFields();
                } else {
                    Toast.makeText(ProfileActivity.this, "Something unexpected happened!", Toast.LENGTH_SHORT).show();
                }
            }
        }, 3000);
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

    // Fetch user details
    private void fetchUser(String nic) {

        stringRequest = new StringRequest(Request.Method.POST, URLs.getUser,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            usrCheckMsg = jsonObject.getString("message");

                            // Creating a new user object
                            if (jsonObject.getString("message").equals("Request Successfully Completed!")) {
                                user = new User(
                                        jsonObject.getString("nic"),
                                        jsonObject.getString("firstName"),
                                        jsonObject.getString("lastName"),
                                        jsonObject.getString("gender"),
                                        jsonObject.getString("dob"),
                                        jsonObject.getString("address"),
                                        jsonObject.getString("mobNumber"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("bloodGroup")
                                );
                            }

                            // Creating a new guardian object
                            if (jsonObject.getString("message").equals("Request Successfully Completed!")) {
                                guardian = new Guardian(
                                        jsonObject.getString("gNIC"),
                                        jsonObject.getString("gName"),
                                        jsonObject.getString("gAddress"),
                                        jsonObject.getString("gConNumber"),
                                        jsonObject.getString("relationship")
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
                params.put("nic", nic);

                return params;
            }
        };
        queue.add(stringRequest);
    }

//        -----------------------------------------------------------------------------------------------

    // Update fields with fetched data
    private void setDataToFields() {
        txtProfNic.setText(user.getNICNumber());
        txtProfName.setText(user.getFirstName() + " " + user.getLastName());
        txtProfGender.setText(user.getGender());
        txtProfDOB.setText(user.getDob());
        txtProfAddress.setText(user.getAddress());
        txtProfMobNumber.setText("+94" + user.getMobNumber());
        txtProfEmail.setText(user.getEmailAddress());
        txtProfBloodGrp.setText(user.getBloodGroup());
        txtProfGNic.setText(guardian.getNICNumber());
        txtProfGName.setText(guardian.getName());
        txtProfGAddress.setText(guardian.getAddress());
        txtProfGConNum.setText("+94" + guardian.getConNumber());
        txtProfRelationship.setText(guardian.getRelationship());

        if (user.getGender().equals("Female")) {
            imgProfAvatar.setImageResource(R.drawable.avatar_woman);
        }
    }
}