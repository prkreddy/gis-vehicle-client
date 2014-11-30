package iiitb.app.geoclientapp;

import iiitb.app.geoclientapp.model.PathData;
import iiitb.app.geoclientapp.model.Vehicle;

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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CreateNewVehicle extends Activity {
	private List<PathData> pathsdata;
	private PathData pathselected;
	private Vehicle vehicle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_vehicle);

		new GetData().execute(getString(R.string.url));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_vehicle, menu);
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
		} else if (id == R.id.newvechile_save) {

			EditText edit = (EditText) findViewById(R.id.createnewvehicle_editText1);
			String vehregno = edit.getText().toString();
			vehicle = new Vehicle();
			vehicle.setPathId(pathselected.getPathId());
			vehicle.setVehicleName(vehregno);
			vehicle.setVehicleRegNo(vehregno);
			vehicle.setAlarmflag(false);
			new HttpAsyncTask().execute(getString(R.string.addvehicleurl));
			invokeClientMod();

		}

		else if (id == R.id.newpath_cancel) {

		}
		return super.onOptionsItemSelected(item);
	}

	void invokeClientMod() {
		Intent intent = new Intent(this, GeofenceClientModeActivity.class);
		startActivity(intent);
	}

	class GetData extends AsyncTask<String, Void, String> {
		private static final int REGISTRATION_TIMEOUT = 3 * 1000;
		private static final int WAIT_TIMEOUT = 30 * 1000;
		private final HttpClient httpclient = new DefaultHttpClient();
		final HttpParams params = httpclient.getParams();
		HttpResponse response;
		private String content = null;
		private boolean error = false;
		private ProgressDialog dialog = new ProgressDialog(
				CreateNewVehicle.this);

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
			Toast toast = Toast.makeText(CreateNewVehicle.this,
					"Error connecting to Server", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();

		}

		protected void onPostExecute(String content) {
			dialog.dismiss();
			Toast toast;
			if (error) {
				toast = Toast.makeText(CreateNewVehicle.this, content,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
			} else {
				displayPaths(content);
			}
		}

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

			HttpPost httpPost = new HttpPost(getString(R.string.addvehicleurl));

			Gson gson = new Gson();
			String json = gson.toJson(vehicle, Vehicle.class);

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

			Spinner sp = (Spinner) findViewById(R.id.newvehicle_spinner);
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

			pathselected = pathsdata.get(pos);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// nothing here
		}
	}

}
