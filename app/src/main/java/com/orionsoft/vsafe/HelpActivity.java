package com.orionsoft.vsafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cdViewCallUs;
    private CardView cdViewEmailUs;
    private CardView cdViewWeb;

//        -----------------------------------------------------------------------------------------------

    //    Activity wide interface - onClick() method
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cdViewCallUs:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:+94774246925"));
                startActivity(intent);
                break;
            case R.id.cdViewEmailUs:
                Intent intent2 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","hello@vsafe.care", null));
                intent2.putExtra(Intent.EXTRA_SUBJECT, "VSafe Care - Help");
                startActivity(Intent.createChooser(intent2, "Choose an Email client:"));
                break;
            case R.id.cdViewWeb:
                Intent intent3 = new Intent(Intent.ACTION_VIEW);
                intent3.setData(Uri.parse("https://vsafe.care"));
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        cdViewCallUs = findViewById(R.id.cdViewCallUs);
        cdViewEmailUs = findViewById(R.id.cdViewEmailUs);
        cdViewWeb = findViewById(R.id.cdViewWeb);

        // Instantiate the setOnClickListener(s) at runtime
        cdViewCallUs.setOnClickListener(this);
        cdViewEmailUs.setOnClickListener(this);
        cdViewWeb.setOnClickListener(this);
    }

//        -----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}