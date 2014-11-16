package iiitb.app.geoclientapp;

import iiitb.app.geoclientapp.model.Geofence;
import iiitb.app.geoclientapp.model.PathData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ClientUserModeActivity extends Activity {


	private List<PathData> pathsdata;
	private GoogleMap googleMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geoclientuser_layout);

		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager()
						.findFragmentById(R.id.map)).getMap();
			}
			// Getting GoogleMap object from the fragment
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);

		}
		// data filler
		new GrabURL().execute(getString(R.string.url));
	}

	private void displayPaths(String response) {

		try {

			Gson gson = new Gson();

			Type typeOfMap = new TypeToken<Map<String, PathData>>() {
			}.getType();
			Map<String, PathData> dataFromJson = gson.fromJson(response,
					typeOfMap);

			List<String> pathnames = new ArrayList<String>();
			pathsdata = new ArrayList<PathData>();

			for (Map.Entry<String, PathData> entry : dataFromJson.entrySet()) {
				pathnames.add(entry.getKey());
				pathsdata.add(entry.getValue());
			}

			Spinner sp = (Spinner) findViewById(R.id.spinner1);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, pathnames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			OnItemSelectedListener spinnerListener = new myOnItemSelectedListener(
					this);
			sp.setOnItemSelectedListener(spinnerListener);
			// sp.setSelection(dftIndex);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class myOnItemSelectedListener implements OnItemSelectedListener {

		Context mContext;

		public myOnItemSelectedListener(Context context) {
			this.mContext = context;
		}

		public void onItemSelected(AdapterView<?> parent, View v, int pos,
				long row) {

			PathData path = pathsdata.get(pos);
			Geofence fencedata = null;
			LatLng latLong = null;
			googleMap.clear();

			MarkerOptions markerOptions = new MarkerOptions();

			// Setting latitude and longitude for the marker

			for (int i = 0; i < path.getLocations().size(); i++) {

				fencedata = path.getLocations().get(i);

				latLong = new LatLng(fencedata.getLatitude(),
						fencedata.getLongitude());
				markerOptions.position(latLong);

				// Adding marker on the Google Map

				 drawMarker(latLong);
			}
			//googleMap.addMarker(markerOptions);
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));

			// Setting the zoom level in the map on last position is clicked
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

			updateVehiclesSpinner(path);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// nothing here
		}
	}

	private void drawMarker(LatLng point) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		googleMap.addMarker(markerOptions);
	}

	void updateVehiclesSpinner(PathData path) {
		String vehiclenames[] = new String[path.getVehicles().size()];

		for (int i = 0; i < path.getVehicles().size(); i++) {
			vehiclenames[i] = path.getVehicles().get(i).getVehicleName();
		}

		Spinner sp = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, vehiclenames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		// OnItemSelectedListener spinnerListener = new
		// myOnItemSelectedListener(
		// this);
		// sp.setOnItemSelectedListener(spinnerListener);

	}

	private class GrabURL extends AsyncTask<String, Void, String> {
		private static final int REGISTRATION_TIMEOUT = 3 * 1000;
		private static final int WAIT_TIMEOUT = 30 * 1000;
		private final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		HttpResponse response;
		private String content = null;
		private boolean error = false;
		private ProgressDialog dialog = new ProgressDialog(
				ClientUserModeActivity.this);

		protected void onPreExecute() {
			dialog.setMessage("Getting your data... Please wait...");
			dialog.show();
		}

		protected String doInBackground(String... urls) {

			String URL = null;

			try {

				URL = urls[0];
				HttpConnectionParams.setConnectionTimeout(params,
						REGISTRATION_TIMEOUT);
				HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
				ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

				HttpGet httpGet = new HttpGet(URL);

				response = httpclient.execute(httpGet);

				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					content = out.toString();
				} else {
					// Closes the connection.
					Log.w("HTTP1:", statusLine.getReasonPhrase());
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				Log.w("HTTP2:", e);
				content = e.getMessage();
				error = true;
				cancel(true);
			} catch (IOException e) {
				Log.w("HTTP3:", e);
				content = e.getMessage();
				error = true;
				cancel(true);
			} catch (Exception e) {
				Log.w("HTTP4:", e);
				content = e.getMessage();
				error = true;
				cancel(true);
			}

			return content;
		}

		protected void onCancelled() {
			dialog.dismiss();
			Toast toast = Toast.makeText(ClientUserModeActivity.this,
					"Error connecting to Server", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();

		}

		protected void onPostExecute(String content) {
			dialog.dismiss();
			Toast toast;
			if (error) {
				toast = Toast.makeText(ClientUserModeActivity.this, content,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
			} else {
				displayPaths(content);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.geoclientuser_layout, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}