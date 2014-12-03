package iiitb.app.geoclientapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class MainActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	public final static String LAT = "com.app.myapp.latitute";
	public final static String LON = "com.app.myapp.longitute";
	LocationClient mLocationClient;
	private TextView addressLabel;
	private TextView locationLabel;
	private Button getLocationBtn;
	private Button disconnectBtn;
	private Button connectBtn;
	private Button showlocaiton;
	private Button addLocations;

	private Button showGeoInterfacae;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		locationLabel = (TextView) findViewById(R.id.locationLabel);
		addressLabel = (TextView) findViewById(R.id.addressLabel);
		getLocationBtn = (Button) findViewById(R.id.getLocation);
		showlocaiton = (Button) findViewById(R.id.showlocation);
		addLocations = (Button) findViewById(R.id.addLocation);
		getLocationBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				displayCurrentLocation();
			}
		});
		disconnectBtn = (Button) findViewById(R.id.disconnect);
		disconnectBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mLocationClient.disconnect();
				locationLabel.setText("Got disconnected....");
			}
		});
		connectBtn = (Button) findViewById(R.id.connect);
		connectBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mLocationClient.connect();
				locationLabel.setText("Got connected....");
			}
		});

		showlocaiton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showLocation();

			}
		});
		// Create the LocationRequest object
		mLocationClient = new LocationClient(this, this, this);

		addLocations.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				addLocation();

			}
		});

		showGeoInterfacae = (Button) findViewById(R.id.geoInterface);

		showGeoInterfacae.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMainGeoClientActivity();
			}
		});
	}

	protected void showMainGeoClientActivity() {
		Intent intent = new Intent(this, ClientUserModeActivity.class);
		startActivity(intent);
	}

	protected void addLocation() {
		Intent intent = new Intent(this, CreateNewPathActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		mLocationClient.connect();
		locationLabel.setText("Got connected....");
	}

	@Override
	protected void onStop() {
		// Disconnect the client.
		mLocationClient.disconnect();
		super.onStop();
		locationLabel.setText("Got disconnected....");
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// Display the error code on failure
		Toast.makeText(this,
				"Connection Failure : " + connectionResult.getErrorCode(),
				Toast.LENGTH_SHORT).show();
	}

	public void displayCurrentLocation() {
		// Get the current location's latitude & longitude
		Location currentLocation = mLocationClient.getLastLocation();
		String msg = "Current Location: "
				+ Double.toString(currentLocation.getLatitude()) + ","
				+ Double.toString(currentLocation.getLongitude());

		// Display the current location in the UI
		locationLabel.setText(msg);

		// To display the current address in the UI
		// (new GetAddressTask(this)).execute(currentLocation);
	}

	public void showLocation() {
		// Get the current location's latitude & longitude
		// Location currentLocation = mLocationClient.getLastLocation();
		// String msg = "Current Location: "
		// + Double.toString(currentLocation.getLatitude()) + ","
		// + Double.toString(currentLocation.getLongitude());
		//
		// // Display the current location in the UI
		// locationLabel.setText(msg);
		try {

			Intent intent = new Intent(this, ProximityMainActivity.class);

			// intent.putExtra(LAT, currentLocation.getLatitude() + "");
			// intent.putExtra(LON, currentLocation.getLongitude() + "");
			startActivity(intent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// To display the current address in the UI
		// (new GetAddressTask(this)).execute(currentLocation);
	}

	/*
	 * Following is a subclass of AsyncTask which has been used to get address
	 * corresponding to the given latitude & longitude.
	 */
	private class GetAddressTask extends AsyncTask<Location, Void, String> {
		Context mContext;

		public GetAddressTask(Context context) {
			super();
			mContext = context;
		}

		/*
		 * When the task finishes, onPostExecute() displays the address.
		 */
		@Override
		protected void onPostExecute(String address) {
			// Display the current address in the UI
			addressLabel.setText(address);
		}

		@Override
		protected String doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
			// Get the current location from the input parameter list
			Location loc = params[0];
			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e1) {
				Log.e("LocationSampleActivity",
						"IO Exception in getFromLocation()");
				e1.printStackTrace();
				return ("IO Exception trying to get address");
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments "
						+ Double.toString(loc.getLatitude()) + " , "
						+ Double.toString(loc.getLongitude())
						+ " passed to address service";
				Log.e("LocationSampleActivity", errorString);
				e2.printStackTrace();
				return errorString;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
				String addressText = String.format(
						"%s, %s, %s",
						// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "",
						// Locality is usually a city
						address.getLocality(),
						// The country of the address
						address.getCountryName());
				// Return the text
				return addressText;
			} else {
				return "No address found";
			}
		}
	}// AsyncTask class
}