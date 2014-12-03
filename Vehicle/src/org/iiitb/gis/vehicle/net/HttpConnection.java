package org.iiitb.gis.vehicle.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.widget.ArrayAdapter;

public class HttpConnection implements Runnable
{
	private String method;
	private String url;
	private List<String> l=null;
	private ArrayAdapter<String> a=null;
	
	public HttpConnection()
	{
		//Nothing goes in here.
	}
	
	public HttpConnection(String method, String url, List<String> l)
	{
		this.method=method;
		this.url=url;
		this.l=l;
	}
	
	public HttpConnection(String method, String url, ArrayAdapter<String> a)
	{
		this.method=method;
		this.url=url;
		this.a=a;
	}
	
	public List<String> sendHttpGetRequest(String url) throws ClientProtocolException, IOException
	{
		HttpClient client=new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response=client.execute(request);
		BufferedReader rd=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		
		String line="";
		List<String> responseLines=new ArrayList<String>();
		while ((line=rd.readLine())!=null)
			responseLines.add(line);
		
		return responseLines;
	}

	@Override
	public void run()
	{
		try
		{
			List<String> tempList=null;
			
			if(method.equals("sendHttpGetRequest"))
				tempList=sendHttpGetRequest(url);
			
			if(l!=null)
			{
				l.clear();
				l.addAll(tempList);
			}
			else if(a!=null)
			{
				a.clear();
				for(String s:tempList)
					a.add(s);
			}
		}
		catch(Exception e)
		{
			//Do something per your wish, like log or something.
		}
	}
}
