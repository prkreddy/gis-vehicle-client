package iiitb.app.geoclientapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class PolyLine extends Activity {

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_poly_line);
//	}

	private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
			-73.998585);
	private static final LatLng TIMES_SQUARE = new LatLng(40.7577, -73.9857);
	private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);

	private GoogleMap googleMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poly_line);
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// check if we have got the googleMap already
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (googleMap != null) {
				addLines();
			}
		}
	}

	private void addLines() {

		googleMap
				.addPolyline((new PolylineOptions())
						.add(TIMES_SQUARE, BROOKLYN_BRIDGE, LOWER_MANHATTAN).width(5).color(Color.BLUE)
						.geodesic(true));
		// move camera to zoom on map
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN,
				13));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poly_line, menu);
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
