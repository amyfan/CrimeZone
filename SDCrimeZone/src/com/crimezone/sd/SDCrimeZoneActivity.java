package com.crimezone.sd;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SDCrimeZoneActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Spinner distanceList = (Spinner)this.findViewById(R.id.distanceList); // the distance dropdown list
        populateSpinnerWithArray(distanceList, R.array.distanceArray);
        Spinner dateList = (Spinner)this.findViewById(R.id.datesList); // the distance dropdown list
        populateSpinnerWithArray(dateList, R.array.dateArray);
    }
    
    public static void populateSpinnerWithArray(Spinner spinner, int stringArrayResId) {  
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(  
                spinner.getContext(),    
                android.R.layout.simple_spinner_item,  
                spinner.getContext().getResources().getStringArray(stringArrayResId));    
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        spinner.setAdapter(adapter);    
    }  
}