package com.orionsoft.vsafe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orionsoft.vsafe.model.User;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtGreet;

    private Button btnLogout;

    private CardView cdViewHelp;
    private CardView cdViewProfile;
    private CardView cdViewSafetyPre;
    private CardView cdViewMedical;
    private CardView cdViewReportCase;

    User user;

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogout:
                logout(); // Logout the user
                break;
            case R.id.cdViewHelp:
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.cdViewProfile:
                Intent intent2 = new Intent(this, ProfileActivity.class);
                startActivity(intent2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.cdViewSafetyPre:
                Intent intent3 = new Intent(this, SafetyPrecautionsActivity.class);
                startActivity(intent3);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.cdViewMedical:
                Intent intent4 = new Intent(this, MedicalDetailsActivity.class);
                startActivity(intent4);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.cdViewReportCase:
                Intent intent5 = new Intent(this, ReportCaseSituationActivity.class);
                startActivity(intent5);
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
        setContentView(R.layout.activity_dashboard);

        // Redirect the user to the Login activity, if the user is not logged in
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(DashboardActivity.this,LoginWithMobActivity.class);
            startActivity(intent);
            finishAffinity(); // Removes the connection of the existing activity to its stack
            finish(); // The method onDestroy() is executed & exit the application
        }

        txtGreet = findViewById(R.id.txtGreet);
        btnLogout = findViewById(R.id.btnLogout);
        cdViewHelp = findViewById(R.id.cdViewHelp);
        cdViewProfile = findViewById(R.id.cdViewProfile);
        cdViewSafetyPre = findViewById(R.id.cdViewSafetyPre);
        cdViewMedical = findViewById(R.id.cdViewMedical);
        cdViewReportCase = findViewById(R.id.cdViewReportCase);

        user = SharedPrefManager.getInstance(this).getUser();
        txtGreet.setText("Hello, " + user.getFirstName() + " !");

        // Instantiate the setOnClickListener(s) at runtime
        btnLogout.setOnClickListener(this);
        cdViewHelp.setOnClickListener(this);
        cdViewProfile.setOnClickListener(this);
        cdViewSafetyPre.setOnClickListener(this);
        cdViewMedical.setOnClickListener(this);
        cdViewReportCase.setOnClickListener(this);
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
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_exit)
                .setTitle("Confirm Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity(); // Removes the connection of the existing activity to its stack
                        finish(); // The method onDestroy() is executed & exit the application
                    }

                })
                .setNegativeButton("No", null).show();
    }

//        -----------------------------------------------------------------------------------------------

    private void logout() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_exit)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                        finishAffinity();
                        finish();
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }

                })
                .setNegativeButton("No", null).show();
    }
}