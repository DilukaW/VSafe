package com.orionsoft.vsafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orionsoft.vsafe.model.Medical;

import java.util.List;

public class MedicalListViewAdapter extends ArrayAdapter<Medical> {

    // The medical details list that will be displayed
    private List<Medical> medicalDetailsList;

    // The context object
    private Context mCtx;

    // Here we are getting the medical details and context
    // So while creating the object of this adapter class we need to give medical details and context
    public MedicalListViewAdapter(List<Medical> medicalDetailsList, Context mCtx) {
        super(mCtx, R.layout.medical_list_items, medicalDetailsList);
        this.medicalDetailsList = medicalDetailsList;
        this.mCtx = mCtx;
    }

    // This method will return the list item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Getting the LayoutInflater
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        // Creating a view with our XML layout
        View listViewItem = inflater.inflate(R.layout.medical_list_items, null, true);

        // Getting text views
        TextView txtMedId = listViewItem.findViewById(R.id.txtMedId);
        TextView txtMedDisease = listViewItem.findViewById(R.id.txtMedDisease);
        TextView txtMedTime = listViewItem.findViewById(R.id.txtMedTime);
        TextView txtMedTreat = listViewItem.findViewById(R.id.txtMedTreat);

        // Getting the medical for the specified position
        Medical medical = medicalDetailsList.get(position);

        // Setting medical values to TextViews
        txtMedId.setText(medical.getId());
        txtMedDisease.setText(medical.getDisease());
        txtMedTime.setText(medical.getTimePeriod());
        txtMedTreat.setText(medical.getUnderTreat());

        //returning the listitem
        return listViewItem;
    }
}