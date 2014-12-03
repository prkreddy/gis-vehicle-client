package iiitb.app.geoclientapp;

import iiitb.app.geoclientapp.model.Geofence;
import iiitb.app.geoclientapp.model.PathData;
import iiitb.app.geoclientapp.util.GeoUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class CreateNewPathActivity extends FragmentActivity {
	private GoogleMap mMap;
	SharedPreferences sharedPreferences;
	int locationCount = 0;
	PathData path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_path);

		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		if (ConnectionResult.SUCCESS != resultCode) {

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this, requestCode);
			dialog.show();

		} else {
			if (mMap == null) {
				mMap = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.createnewpath_fragment1)).getMap();
				mMap.setMyLocationEnabled(true);

			}

			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			sharedPreferences = getSharedPreferences("location", 0);

		}
		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				locationCount++;

				// Drawing marker on the map
				GeoUtil.drawMarker(mMap, point, point + "");

				// drawCircle(point);
				GeoUtil.drawCircle(mMap, point, 3.0);

				/** Opening the editor object to write data to sharedPreferences */
				SharedPreferences.Editor editor = sharedPreferences.edit();

				// Storing the latitude for the i-th location
				editor.putString("lat" + Integer.toString((locationCount - 1)),
						Double.toString(point.latitude));

				// Storing the longitude for the i-th location
				editor.putString("lng" + Integer.toString((locationCount - 1)),
						Double.toString(point.longitude));

				// Storing the count of locations or marker count
				editor.putInt("locationCount", locationCount);

				/** Storing the zoom level to the shared preferences */
				editor.putString("zoom",
						Float.toString(mMap.getCameraPosition().zoom));

				/** Saving the values stored in the shared preferences */
				editor.commit();

				Toast.makeText(getBaseContext(), "Marker is added to the Map",
						Toast.LENGTH_SHORT).show();

			}
		});

		mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {

				clear();
			}
		});

	}

	void clear() {

		mMap.clear();

		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.clear();

		editor.commit();

		locationCount = 0;

	}

	private void drawMarker(LatLng point) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		mMap.addMarker(markerOptions);
	}

	private void drawCircle(LatLng point, double radius) {

		// Instantiating CircleOptions to draw a circle around the marker
		CircleOptions circleOptions = new CircleOptions();

		// Specifying the center of the circle
		circleOptions.center(point);

		// Radius of the circle
		circleOptions.radius(radius);

		// Border color of the circle
		circleOptions.strokeColor(Color.BLACK);

		// Fill color of the circle
		circleOptions.fillColor(0x30ff0000);

		// Border width of the circle
		circleOptions.strokeWidth(2);

		// Adding the circle to the GoogleMap
		mMap.addCircle(circleOptions);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_path, menu);
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
		} else if (id == R.id.newpath_save) {
			if (locationCount != 0) {

				String lat = "";
				String lng = "";

				EditText edit = (EditText) findViewById(R.id.createnewpath_editText1);
				String pathName = edit.getText().toString();

				path = new PathData();
				path.setPathName(pathName);
				path.setPathLen(150);

				Geofence fence = null;

				// Iterating through all the locations stored
				for (int i = 0; i < locationCount; i++) {
					fence = new Geofence();
					// Getting the latitude of the i-th location
					lat = sharedPreferences.getString("lat" + i, "0");

					// Getting the longitude of the i-th location
					lng = sharedPreferences.getString("lng" + i, "0");
					fence.setLatitude(Double.valueOf(lat));
					fence.setLongitude(Double.valueOf(lng));
					fence.setFenceRadius(2);
					path.getLocations().add(fence);
				}

				new HttpAsyncTask().execute(getString(R.string.addpathurl));
				invokeClientMod();
			}
		} else if (id == R.id.newpath_cancel) {

			clear();

		}
		return super.onOptionsItemSelected(item);
	}

	void invokeClientMod() {
		Intent intent = new Intent(this, GeofenceClientModeActivity.class);
		startActivity(intent);
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
			clear();
		}
	}

	public String POST() {
		InputStream inputStream = null;
		String result = "";
		try {

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(getString(R.string.addpathurl));

			Gson gson = new Gson();
			String json = gson.toJson(path, PathData.class);

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
}
