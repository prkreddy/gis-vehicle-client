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

public class ClientUserModeActivity extends Activity {

	private List<PathData> pathsdata;
	private GoogleMap googleMap;
	private String SelectPath = "<Select Path>";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geoclientuser_layout);

		googleMap = GeoUtil.getGoogleMap(this, getFragmentManager(), R.id.map);
		// data filler
		GetDataURL getDataUrl = new GetDataURL(this);
		getDataUrl.execute(getString(R.string.url));

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

			pathnames.add(0, SelectPath);

			Spinner sp = (Spinner) findViewById(R.id.spinner1);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, pathnames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			OnItemSelectedListener spinnerListener = new SpinnerItemSelectedListener(
					pathsdata, googleMap, this,
					(Spinner) findViewById(R.id.spinner2));
			sp.setOnItemSelectedListener(spinnerListener);

		} catch (Exception e) {
			e.printStackTrace();
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
		} else if (id == R.id.Admin_mode) {

			Intent intent = new Intent(this, GeofenceClientModeActivity.class);
			startActivity(intent);

		}
		return super.onOptionsItemSelected(item);
	}
}