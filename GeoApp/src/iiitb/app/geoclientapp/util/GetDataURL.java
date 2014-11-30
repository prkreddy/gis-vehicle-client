package iiitb.app.geoclientapp.util;

import iiitb.app.geoclientapp.ClientUserModeActivity;
import iiitb.app.geoclientapp.GeofenceClientModeActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class GetDataURL extends AsyncTask<String, Void, String> {

	private static final int REGISTRATION_TIMEOUT = 3 * 1000;
	private static final int WAIT_TIMEOUT = 30 * 1000;
	private final HttpClient httpclient = new DefaultHttpClient();
	final HttpParams params = httpclient.getParams();
	HttpResponse response;
	private String content = null;
	private boolean error = false;
	private ProgressDialog dialog;
	Activity activity = null;

	public GetDataURL(Activity context) {
		// TODO Auto-generated constructor stub
		this.activity = context;
		dialog = new ProgressDialog(context);

	}

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
		Toast toast = Toast.makeText(activity, "Error connecting to Server",
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, 25, 400);
		toast.show();

	}

	protected void onPostExecute(String content) {
		dialog.dismiss();
		Toast toast;
		if (error) {
			toast = Toast.makeText(activity, content, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 25, 400);
			toast.show();
		} else {
			if (activity instanceof ClientUserModeActivity) {
				((ClientUserModeActivity) activity).displayPaths(content);
			} else if (activity instanceof GeofenceClientModeActivity) {
				((GeofenceClientModeActivity) activity).displayPaths(content);
			}
		}
	}

}
