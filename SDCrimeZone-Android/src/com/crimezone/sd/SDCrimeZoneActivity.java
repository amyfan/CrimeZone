package com.crimezone.sd;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SDCrimeZoneActivity extends Activity {
	
	public static Location currLocation;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.createLocationManager();
        this.initializeApp();
    }
    
    /**
     * initializeApp
     * Take user to landing page for SD Crime Zone.
     * Populate the dropdown boxes (Distance and Date)
     */
    public void initializeApp() {
    	setContentView(R.layout.main);
    	//get current GPS coordinates and set as default for app
    	EditText currLocationText = (EditText)this.findViewById(R.id.addressText);
    	currLocationText.setText("Current Location");
    	// the distance dropdown list
    	Spinner distanceList = (Spinner)this.findViewById(R.id.distanceList); 
        populateSpinnerWithArray(distanceList, R.array.distanceArray);
        // the distance dropdown list
        Spinner dateList = (Spinner)this.findViewById(R.id.datesList); 
        populateSpinnerWithArray(dateList, R.array.dateArray);
        
        /* set onclick functionality for button */
        final Button button = (Button) findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                this.sendHttpRequestToServer(v);
            }

            //TODO: should return results from server (JSON, XML, or whatever)
			private void sendHttpRequestToServer(View v) {
				//get the current GPS coordinates, distance, and dates selected
				
			}
        });

    }
    
    public void createLocationManager() {
    	// Acquire a reference to the system Location Manager
    	LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    	// Define a listener that responds to location updates
    	LocationListener locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	      // Called when a new location is found by the network location provider.
    	      updateAddressWithCurrentLocation(location);
    	    }

    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}
    	  };

    	// Register the listener with the Location Manager to receive location updates
    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }
    
    public void updateAddressWithCurrentLocation(Location location) {
    	currLocation = location;
	}

	/**
     * populateSpinnerWithArray
     * Populates dropdown boxes with options, based on string arrays.
     * @param spinner
     * @param stringArrayResId
     */
    public static void populateSpinnerWithArray(Spinner spinner, int stringArrayResId) {  
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(  
                spinner.getContext(),    
                android.R.layout.simple_spinner_item,  
                spinner.getContext().getResources().getStringArray(stringArrayResId));    
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
        spinner.setAdapter(adapter);    
    }  
}