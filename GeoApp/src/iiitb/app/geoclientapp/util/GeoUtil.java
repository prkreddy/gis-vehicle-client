package iiitb.app.geoclientapp.util;

import iiitb.app.geoclientapp.R;
import iiitb.app.geoclientapp.model.PathData;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class GeoUtil {

	public static void drawCircle(GoogleMap googleMap, LatLng point) {

		// Instantiating CircleOptions to draw a circle around the marker
		CircleOptions circleOptions = new CircleOptions();

		// Specifying the center of the circle
		circleOptions.center(point);

		// Radius of the circle
		circleOptions.radius(20);

		// Border color of the circle
		circleOptions.strokeColor(Color.BLACK);

		// Fill color of the circle
		circleOptions.fillColor(0x30ff0000);

		// Border width of the circle
		circleOptions.strokeWidth(2);

		// Adding the circle to the GoogleMap
		googleMap.addCircle(circleOptions);

	}

	public static void drawMarker(GoogleMap googleMap, LatLng point) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		googleMap.addMarker(markerOptions);
	}

	public static GoogleMap getGoogleMap(Activity activity,
			FragmentManager fgm, int mapId) {
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(activity.getBaseContext());
		GoogleMap googleMap = null;
		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
					activity, requestCode);
			dialog.show();

		} else { // Google Play Services are available

			if (googleMap == null) {
				googleMap = ((MapFragment) fgm.findFragmentById(mapId))
						.getMap();
			}
			// Getting GoogleMap object from the fragment
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// Enabling MyLocation Layer of Google Map
			googleMap.setMyLocationEnabled(true);

		}
		return googleMap;
	}

	public static void addLines(GoogleMap googleMap, List<LatLng> points) {

		PolylineOptions options = new PolylineOptions();
		for (LatLng latlng : points) {
			options.add(latlng);
		}

		googleMap
				.addPolyline(options.width(5).color(Color.BLUE).geodesic(true));
		// move camera to zoom on map
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
				points.get(points.size() - 1), 13));
	}

	public static void updateVehiclesSpinner(Activity activity, PathData path,
			Spinner sp) {
		String vehiclenames[] = new String[path.getVehicles().size()];

		for (int i = 0; i < path.getVehicles().size(); i++) {
			vehiclenames[i] = path.getVehicles().get(i).getVehicleRegNo();
		}

		// Spinner sp = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, vehiclenames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);

	}

}
