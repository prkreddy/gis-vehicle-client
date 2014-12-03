package org.iiitb.gis.vehicle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.iiitb.gis.vehicle.net.Vehicle;
import org.iiitb.gis.vehicle.net.VehicleLocation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends Activity {

	TextView tvStatus;
	Spinner etRegNo;
	List<Vehicle> vehicles;
	int vehiclepostionSelected;
	String SELECT_REGNO = "Select vehicleRegNo";

	VehicleLocation vlocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvStatus = (TextView) findViewById(R.id.TextView03);

		try {
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			LocationListener locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {

					double latitude = location.getLatitude();
					double longitude = location.getLongitude();

					String msg = "Location changed : " + " Latitude: "
							+ latitude + ", Longitude: " + longitude;
					Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT)
							.show();
					tvStatus.setText(msg);

					try {
						vlocation = new VehicleLocation();
						vlocation.setLatitude(latitude);
						vlocation.setLongitude(longitude);
						if (vehiclepostionSelected != 0) {
							new HttpAsyncTask()
									.execute(getString(R.string.udpateVehicleLocation));
						}

					} catch (Exception e) {
						Toast.makeText(
								getBaseContext(),
								"Location upload failure! Please check your network connection.",
								Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub

				}
			};

			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
					locationListener);
		} catch (Exception e) {
			e.printStackTrace();
		}

		new GrabURL().execute(getString(R.string.getvehicles));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			return POST();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
		}
	}

	public String POST() {
		InputStream inputStream = null;
		String result = "";
		try {

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(
					getString(R.string.udpateVehicleLocation));

			Gson gson = new Gson();

			vehicles.get(vehiclepostionSelected - 1).setVehicleLocation(
					vlocation);
			String json = gson.toJson(vehicles.get(vehiclepostionSelected - 1),
					Vehicle.class);

			StringEntity se = new StringEntity(json);

			httpPost.setEntity(se);

			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse httpResponse = httpclient.execute(httpPost);

			inputStream = httpResponse.getEntity().getContent();

			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	private void displayPaths(String response) {

		try {

			Gson gson = new Gson();

			Type typeOfMap = new TypeToken<Map<String, Vehicle>>() {
			}.getType();
			Map<String, Vehicle> dataFromJson = gson.fromJson(response,
					typeOfMap);

			List<String> vehiclenames = new ArrayList<String>();
			vehicles = new ArrayList<Vehicle>();
			for (Map.Entry<String, Vehicle> entry : dataFromJson.entrySet()) {
				vehiclenames.add(entry.getKey());
				vehicles.add(entry.getValue());
			}
			vehiclenames.add(0, SELECT_REGNO);
			etRegNo = (Spinner) findViewById(R.id.spinner2);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, vehiclenames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			etRegNo.setAdapter(adapter);
			OnItemSelectedListener spinnerListener = new myOnItemSelectedListener(
					this);
			etRegNo.setOnItemSelectedListener(spinnerListener);
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

			vehiclepostionSelected = pos;

			/*if (pos != 0 && vlocation != null && vlocation.getLatitude() != 0
					&& vlocation.getLongitude() != 0)
				new HttpAsyncTask()
						.execute(getString(R.string.udpateVehicleLocation));*/
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// nothing here
		}
	}

	private class GrabURL extends AsyncTask<String, Void, String> {
		private static final int REGISTRATION_TIMEOUT = 3 * 1000;
		private static final int WAIT_TIMEOUT = 30 * 1000;
		private final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		HttpResponse response;
		private String content = null;
		private boolean error = false;
		private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

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
			Toast toast = Toast.makeText(MainActivity.this,
					"Error connecting to Server", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();

		}

		protected void onPostExecute(String content) {
			dialog.dismiss();
			Toast toast;
			if (error) {
				toast = Toast.makeText(MainActivity.this, content,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
			} else {
				displayPaths(content);
			}
		}

	}
}
