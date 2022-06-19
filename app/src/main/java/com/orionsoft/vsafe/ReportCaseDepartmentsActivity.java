package com.orionsoft.vsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.orionsoft.vsafe.model.Case;
import com.orionsoft.vsafe.model.Department;
import com.orionsoft.vsafe.model.User;

public class ReportCaseDepartmentsActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox chBoxRCDepartPolice;
    private CheckBox chBoxRCDepartHospital;
    private CheckBox chBoxRCDepartFireBr;
    private CheckBox chBoxRCDepartDMC;
    private CheckBox chBoxRCDepartMWCA;

    private Button btnRCDepartNext;

    private TextView txtRCDepartTitle;

    Case aCase;
    Department department;

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRCDepartNext:
                if (validateSelections() == true) {
                    fetchSelection(); // Fetch selections
                    Intent intent = new Intent(this, ReportCaseDetailsActivity.class);
                    intent.putExtra("caseObj", aCase); // Pass Case object from this to ReportCaseDetailsActivity
                    intent.putExtra("departObj", department); // Pass Department object from this to ReportCaseDetailsActivity
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    txtRCDepartTitle.setError("Select at least one department!");
                    Toast.makeText(this, "Select at least one department!", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_report_case_departments);

        // Retrieve objects from the previous activity
        aCase = (Case) getIntent().getSerializableExtra("caseObj");

        department = new Department(); // Department object initialization

        btnRCDepartNext = findViewById(R.id.btnRCDepartNext);
        txtRCDepartTitle = findViewById(R.id.txtRCDepartTitle);

        chBoxRCDepartPolice = findViewById(R.id.chBoxRCDepartPolice);
        chBoxRCDepartHospital = findViewById(R.id.chBoxRCDepartHospital);
        chBoxRCDepartFireBr = findViewById(R.id.chBoxRCDepartFireBr);
        chBoxRCDepartDMC = findViewById(R.id.chBoxRCDepartDMC);
        chBoxRCDepartMWCA = findViewById(R.id.chBoxRCDepartMWCA);

        // Instantiate the setOnClickListener(s) at runtime
        btnRCDepartNext.setOnClickListener(this);


    }

//        -----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

//        -----------------------------------------------------------------------------------------------

    // Validate selections
    private boolean validateSelections() {
        if (chBoxRCDepartPolice.isChecked() || chBoxRCDepartHospital.isChecked() || chBoxRCDepartFireBr.isChecked() || chBoxRCDepartDMC.isChecked() || chBoxRCDepartMWCA.isChecked()) {
            return true;
        } else {
            return false;
        }
    }

//        -----------------------------------------------------------------------------------------------

    // Fetch user selections
    private void fetchSelection() {
        if (chBoxRCDepartPolice.isChecked()) {
            department.setPolice(1);
        } else {
            department.setPolice(0);
        }

        if (chBoxRCDepartHospital.isChecked()) {
            department.setHospital(1);
        } else {
            department.setHospital(0);
        }

        if (chBoxRCDepartFireBr.isChecked()) {
            department.setFireBrigade(1);
        } else {
            department.setFireBrigade(0);
        }

        if (chBoxRCDepartDMC.isChecked()) {
            department.setDmc(1);
        } else {
            department.setDmc(0);
        }
        
        if (chBoxRCDepartMWCA.isChecked()) {
            department.setMwca(1);
        } else {
            department.setMwca(0);
        }
    }
}