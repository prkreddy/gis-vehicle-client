package iiitb.app.geoclientapp;

import iiitb.app.geoclientapp.model.PathData;
import iiitb.app.geoclientapp.util.GeoUtil;
import iiitb.app.geoclientapp.util.GetDataURL;
import iiitb.app.geoclientapp.util.SpinnerItemSelectedListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GeofenceClientModeActivity extends Activity {

	private List<PathData> pathsdata;
	private GoogleMap googleMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geofenceadmin_layout);

		googleMap = GeoUtil.getGoogleMap(this, getFragmentManager(), R.id.map2);
		// data filler
		new GetDataURL(this).execute(getString(R.string.url));
	}

	public void displayPaths(String response) {

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

			Spinner sp = (Spinner) findViewById(R.id.adminmode_basic_spinner1);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, pathnames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			OnItemSelectedListener spinnerListener = new SpinnerItemSelectedListener(
					this, pathsdata, googleMap, this,
					(Spinner) findViewById(R.id.adminmode_basic_spinner2));
			sp.setOnItemSelectedListener(spinnerListener);
			// sp.setSelection(dftIndex);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.geofence_admin_mode, menu);
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
		} else if (id == R.id.adminmode_newPath) {
			Intent intent = new Intent(this, CreateNewPathActivity.class);
			startActivity(intent);
		} else if (id == R.id.user_mode) {
			Intent intent = new Intent(this, ClientUserModeActivity.class);
			startActivity(intent);
		} else if (id == R.id.adminmode_newVehicle) {
			Intent intent = new Intent(this, CreateNewVehicle.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
