package com.orionsoft.vsafe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orionsoft.vsafe.model.Case;

public class ReportCaseSituationActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton raBtnRCSitAccident;
    private RadioButton raBtnRCSitAttack;
    private RadioButton raBtnRCSitRobbery;
    private RadioButton raBtnRCSitKidnap;
    private RadioButton raBtnRCSitMedical;
    private RadioButton raBtnRCSitSexHarass;
    private RadioButton raBtnRCSitFire;
    private RadioButton raBtnRCSitNatural;
    private RadioButton raBtnRCSitOther;

    private CardView cdViewRCSitAccident;
    private CardView cdViewRCSitAttack;
    private CardView cdViewRCSitRobbery;
    private CardView cdViewRCSitKidnap;
    private CardView cdViewRCSitMedical;
    private CardView cdViewRCSitSexHarass;
    private CardView cdViewRCSitFire;
    private CardView cdViewRCSitNatural;
    private CardView cdViewRCSitOther;

    private Button btnRCSituationNext;

    private TextView txtRCSitTitle;

    Case aCase;

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRCSituationNext:
                if (validateSelection() == true) {
                    Intent intent = new Intent(this, ReportCaseDepartmentsActivity.class);
                    intent.putExtra("caseObj", aCase); // Pass Case object from this to ReportCaseDepartmentsActivity
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    txtRCSitTitle.setError("Select the situation!");
                    Toast.makeText(this, "Select the situation!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.raBtnRCSitAccident:
                aCase.setSituation("Accident");
                cdViewRCSitAccident.setCardBackgroundColor(Color.parseColor("#B0E8F6"));
                raBtnRCSitAttack.setChecked(false);
                raBtnRCSitRobbery.setChecked(false);
                raBtnRCSitKidnap.setChecked(false);
                raBtnRCSitMedical.setChecked(false);
                raBtnRCSitSexHarass.setChecked(false);
                raBtnRCSitFire.setChecked(false);
                raBtnRCSitNatural.setChecked(false);
                raBtnRCSitOther.setChecked(false);
                cdViewRCSitAttack.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitRobbery.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitKidnap.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitMedical.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitSexHarass.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitFire.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitNatural.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitOther.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.raBtnRCSitAttack:
                aCase.setSituation("Attack");
                cdViewRCSitAttack.setCardBackgroundColor(Color.parseColor("#B0E8F6"));
                raBtnRCSitAccident.setChecked(false);
                raBtnRCSitRobbery.setChecked(false);
                raBtnRCSitKidnap.setChecked(false);
                raBtnRCSitMedical.setChecked(false);
                raBtnRCSitSexHarass.setChecked(false);
                raBtnRCSitFire.setChecked(false);
                raBtnRCSitNatural.setChecked(false);
                raBtnRCSitOther.setChecked(false);
                cdViewRCSitAccident.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitRobbery.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitKidnap.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitMedical.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitSexHarass.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitFire.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitNatural.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitOther.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.raBtnRCSitRobbery:
                aCase.setSituation("Robbery");
                cdViewRCSitRobbery.setCardBackgroundColor(Color.parseColor("#B0E8F6"));
                raBtnRCSitAccident.setChecked(false);
                raBtnRCSitAttack.setChecked(false);
                raBtnRCSitKidnap.setChecked(false);
                raBtnRCSitMedical.setChecked(false);
                raBtnRCSitSexHarass.setChecked(false);
                raBtnRCSitFire.setChecked(false);
                raBtnRCSitNatural.setChecked(false);
                raBtnRCSitOther.setChecked(false);
                cdViewRCSitAttack.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitAccident.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitKidnap.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitMedical.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitSexHarass.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitFire.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitNatural.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitOther.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.raBtnRCSitKidnap:
                aCase.setSituation("Kidnap");
                cdViewRCSitKidnap.setCardBackgroundColor(Color.parseColor("#B0E8F6"));
                raBtnRCSitAccident.setChecked(false);
                raBtnRCSitRobbery.setChecked(false);
                raBtnRCSitAttack.setChecked(false);
                raBtnRCSitMedical.setChecked(false);
                raBtnRCSitSexHarass.setChecked(false);
                raBtnRCSitFire.setChecked(false);
                raBtnRCSitNatural.setChecked(false);
                raBtnRCSitOther.setChecked(false);
                cdViewRCSitAttack.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitRobbery.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitAccident.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitMedical.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitSexHarass.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitFire.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitNatural.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitOther.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.raBtnRCSitMedical:
                aCase.setSituation("Medical Emergency");
                cdViewRCSitMedical.setCardBackgroundColor(Color.parseColor("#B0E8F6"));
                raBtnRCSitAccident.setChecked(false);
                raBtnRCSitRobbery.setChecked(false);
                raBtnRCSitKidnap.setChecked(false);
                raBtnRCSitAttack.setChecked(false);
                raBtnRCSitSexHarass.setChecked(false);
                raBtnRCSitFire.setChecked(false);
                raBtnRCSitNatural.setChecked(false);
                raBtnRCSitOther.setChecked(false);
                cdViewRCSitAttack.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitRobbery.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitKidnap.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitAccident.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitSexHarass.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitFire.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitNatural.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitOther.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.raBtnRCSitSexHarass:
                aCase.setSituation("Sexual Harassment");
                cdViewRCSitSexHarass.setCardBackgroundColor(Color.parseColor("#B0E8F6"));
                raBtnRCSitAccident.setChecked(false);
                raBtnRCSitRobbery.setChecked(false);
                raBtnRCSitKidnap.setChecked(false);
                raBtnRCSitMedical.setChecked(false);
                raBtnRCSitAttack.setChecked(false);
                raBtnRCSitFire.setChecked(false);
                raBtnRCSitNatural.setChecked(false);
                raBtnRCSitOther.setChecked(false);
                cdViewRCSitAttack.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitRobbery.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitKidnap.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitMedical.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitAccident.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitFire.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitNatural.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitOther.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.raBtnRCSitFire:
                aCase.setSituation("Fire");
                cdViewRCSitFire.setCardBackgroundColor(Color.parseColor("#B0E8F6"));
                raBtnRCSitAccident.setChecked(false);
                raBtnRCSitRobbery.setChecked(false);
                raBtnRCSitKidnap.setChecked(false);
                raBtnRCSitMedical.setChecked(false);
                raBtnRCSitSexHarass.setChecked(false);
                raBtnRCSitAttack.setChecked(false);
                raBtnRCSitNatural.setChecked(false);
                raBtnRCSitOther.setChecked(false);
                cdViewRCSitAttack.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitRobbery.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitKidnap.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitMedical.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitSexHarass.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitAccident.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitNatural.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitOther.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.raBtnRCSitNatural:
                aCase.setSituation("Natural Disaster");
                cdViewRCSitNatural.setCardBackgroundColor(Color.parseColor("#B0E8F6"));
                raBtnRCSitAccident.setChecked(false);
                raBtnRCSitRobbery.setChecked(false);
                raBtnRCSitKidnap.setChecked(false);
                raBtnRCSitMedical.setChecked(false);
                raBtnRCSitSexHarass.setChecked(false);
                raBtnRCSitFire.setChecked(false);
                raBtnRCSitAttack.setChecked(false);
                raBtnRCSitOther.setChecked(false);
                cdViewRCSitAttack.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitRobbery.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitKidnap.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitMedical.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitSexHarass.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitFire.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitAccident.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitOther.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.raBtnRCSitOther:
                aCase.setSituation("Other");
                cdViewRCSitOther.setCardBackgroundColor(Color.parseColor("#B0E8F6"));
                raBtnRCSitAccident.setChecked(false);
                raBtnRCSitRobbery.setChecked(false);
                raBtnRCSitKidnap.setChecked(false);
                raBtnRCSitMedical.setChecked(false);
                raBtnRCSitSexHarass.setChecked(false);
                raBtnRCSitFire.setChecked(false);
                raBtnRCSitNatural.setChecked(false);
                raBtnRCSitAttack.setChecked(false);
                cdViewRCSitAttack.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitRobbery.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitKidnap.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitMedical.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitSexHarass.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitFire.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitNatural.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cdViewRCSitAccident.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            default:
                break;
        }
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_case_situation);

        btnRCSituationNext = findViewById(R.id.btnRCSituationNext);

        raBtnRCSitAccident = findViewById(R.id.raBtnRCSitAccident);
        raBtnRCSitAttack = findViewById(R.id.raBtnRCSitAttack);
        raBtnRCSitRobbery = findViewById(R.id.raBtnRCSitRobbery);
        raBtnRCSitKidnap = findViewById(R.id.raBtnRCSitKidnap);
        raBtnRCSitMedical = findViewById(R.id.raBtnRCSitMedical);
        raBtnRCSitSexHarass = findViewById(R.id.raBtnRCSitSexHarass);
        raBtnRCSitFire = findViewById(R.id.raBtnRCSitFire);
        raBtnRCSitNatural = findViewById(R.id.raBtnRCSitNatural);
        raBtnRCSitOther = findViewById(R.id.raBtnRCSitOther);

        cdViewRCSitAccident = findViewById(R.id.cdViewRCSitAccident);
        cdViewRCSitAttack = findViewById(R.id.cdViewRCSitAttack);
        cdViewRCSitRobbery = findViewById(R.id.cdViewRCSitRobbery);
        cdViewRCSitKidnap = findViewById(R.id.cdViewRCSitKidnap);
        cdViewRCSitMedical = findViewById(R.id.cdViewRCSitMedical);
        cdViewRCSitSexHarass = findViewById(R.id.cdViewRCSitSexHarass);
        cdViewRCSitFire = findViewById(R.id.cdViewRCSitFire);
        cdViewRCSitNatural = findViewById(R.id.cdViewRCSitNatural);
        cdViewRCSitOther = findViewById(R.id.cdViewRCSitOther);

        txtRCSitTitle = findViewById(R.id.txtRCSitTitle);


        aCase = new Case(); // Case object initialization


        // Instantiate the setOnClickListener(s) at runtime
        btnRCSituationNext.setOnClickListener(this);
        raBtnRCSitAccident.setOnClickListener(this);
        raBtnRCSitAttack.setOnClickListener(this);
        raBtnRCSitRobbery.setOnClickListener(this);
        raBtnRCSitKidnap.setOnClickListener(this);
        raBtnRCSitMedical.setOnClickListener(this);
        raBtnRCSitSexHarass.setOnClickListener(this);
        raBtnRCSitFire.setOnClickListener(this);
        raBtnRCSitNatural.setOnClickListener(this);
        raBtnRCSitOther.setOnClickListener(this);
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_exit)
                .setTitle("Confirm Cancel")
                .setMessage("Are you sure you would like to cancel?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // The method onDestroy() is executed & exit the activity
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }

                })
                .setNegativeButton("No", null).show();
    }

//        -----------------------------------------------------------------------------------------------

    // Validate selection
    private boolean validateSelection() {
        if (raBtnRCSitAccident.isChecked() || raBtnRCSitAttack.isChecked() || raBtnRCSitRobbery.isChecked() || raBtnRCSitKidnap.isChecked() || raBtnRCSitMedical.isChecked() || raBtnRCSitSexHarass.isChecked() || raBtnRCSitFire.isChecked() || raBtnRCSitNatural.isChecked() || raBtnRCSitOther.isChecked()) {
            return true;
        } else {
            return false;
        }
    }
}