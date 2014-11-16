package iiitb.app.geoclientapp;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Main3Activity extends FragmentActivity {
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main3);
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		if (ConnectionResult.SUCCESS != resultCode) {

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this, requestCode);
			dialog.show();

		} else {

			showCurrentLocation();
		}
	}	

	void showCurrentLocation() {
		try {

			if (mMap == null) {
				mMap = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();
			}
			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

				Intent intent = getIntent();
				String lat = intent.getStringExtra(MainActivity.LAT);
				String lon = intent.getStringExtra(MainActivity.LON);

				LatLng currentLoc = new LatLng(Double.valueOf(lat),
						Double.valueOf(lon));
				mMap.addMarker(new MarkerOptions().position(currentLoc)
						.title("My Location").draggable(true));

				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,
						13));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
