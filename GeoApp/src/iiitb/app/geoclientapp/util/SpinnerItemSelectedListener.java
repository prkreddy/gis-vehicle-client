package iiitb.app.geoclientapp.util;

import iiitb.app.geoclientapp.model.Geofence;
import iiitb.app.geoclientapp.model.PathData;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SpinnerItemSelectedListener implements OnItemSelectedListener {

	private List<PathData> pathsdata;

	GoogleMap googleMap;

	Activity activity;

	Spinner nextsp;

	public SpinnerItemSelectedListener(List<PathData> pathsdata,
			GoogleMap googleMap, Activity activity, Spinner spinner) {
		this.pathsdata = pathsdata;
		this.googleMap = googleMap;
		this.activity = activity;

		this.nextsp = spinner;
	}

	public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {

		if (pos != 0) {
			PathData path = pathsdata.get(pos - 1);
			Geofence fencedata = null;
			LatLng latLong = null;
			googleMap.clear();

			MarkerOptions markerOptions = new MarkerOptions();
			List<LatLng> points = new ArrayList<LatLng>();
			// Setting latitude and longitude for the marker

			for (int i = 0; i < path.getLocations().size(); i++) {

				fencedata = path.getLocations().get(i);

				latLong = new LatLng(fencedata.getLatitude(),
						fencedata.getLongitude());
				markerOptions.position(latLong);
				points.add(latLong);
				// Adding marker on the Google Map

				// drawMarker(latLong);
				GeoUtil.drawCircle(googleMap, latLong);
			}
			GeoUtil.addLines(googleMap, points);
			// googleMap.addMarker(markerOptions);
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));

			// Setting the zoom level in the map on last position is clicked
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

			updateVehiclesSpinner(path);
		} else {
			updateVehiclesSpinner2();
		}
	}

	void updateVehiclesSpinner(PathData path) {

		List<String> vehiclenames = new ArrayList<String>();

		for (int i = 0; i < path.getVehicles().size(); i++) {
			vehiclenames.add(path.getVehicles().get(i).getVehicleRegNo());
		}
		vehiclenames.add(0, "<All Vehicles>");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, vehiclenames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nextsp.setAdapter(adapter);

		OnItemSelectedListener spinnerListener = new SpinnerVehicleUpdateItemSelectedListener(
				path, googleMap, activity);
		nextsp.setOnItemSelectedListener(spinnerListener);
	}

	void updateVehiclesSpinner2() {

		List<String> vehiclenames = new ArrayList<String>();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, vehiclenames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nextsp.setAdapter(adapter);
		googleMap.clear();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// nothing here
	}
}
