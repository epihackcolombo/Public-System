package org.cosmic.mobuzz.general.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.cosmic.mobuzz.general.pojo.ResponsePojo;
import org.cosmic.mobuzz.general.util.GlobalMethods;
import org.cosmic.mobuzz.general.util.GlobalVariables;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class HttpUpload extends Activity {

	Context c = HttpUpload.this;

	private final static String TAG = HttpUpload.class.getName();

	public static String uploadData(String jString, String targetURL) {

		URL url;
		HttpURLConnection connection = null;

		try {
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(jString.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setConnectTimeout(GlobalVariables.CONN_TIMEOUT);

			//connection.setUseCaches(false);
			connection.setUseCaches(true);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(jString);

			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			StringBuffer response = new StringBuffer();

			while ((line = rd.readLine()) != null) {
				response.append(line);
				//response.append('\r');
			}
			rd.close();

			statPrint(connection.getResponseCode(), response.toString());
			return response.toString();

		} catch (MalformedURLException ex) {

			statPrint("MalformedURLException", ex.getMessage());
			return GlobalVariables.CONN_EXCEPTION;

		} catch (java.net.ConnectException ex1) {
			statPrint("ConnectException", ex1.getMessage());
			return GlobalVariables.CONN_EXCEPTION;

		} catch (IOException ex2) {

			statPrint("IOException", ex2.getMessage());
			
			String problem = ex2.getClass().getName();
			
			if(GlobalMethods.validateString(problem))
			{
				if(problem.contains("java.net.SocketTimeoutException"))
				{
					return GlobalVariables.CONN_EXCEPTION;
				}
				else
				{
					return GlobalVariables.OTHER_EXCEPTION;
				}

			}
			else
			{
				return GlobalVariables.OTHER_EXCEPTION;
			}

		}
		catch(Exception ex3)
		{
			statPrint("Exception", ex3.getMessage());
			return GlobalVariables.OTHER_EXCEPTION;
		}

	}

	//open http upload stream to a destination
	//enable data caching in order to reduce the number of actual network calls	
	public static String uploadDataCache(String jString, String targetURL) {

		URL url;
		HttpURLConnection connection = null;

		try {
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(jString.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setConnectTimeout(GlobalVariables.CONN_TIMEOUT);

			connection.setUseCaches(true);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(jString);

			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			StringBuffer response = new StringBuffer();

			while ((line = rd.readLine()) != null) {
				response.append(line);
				//response.append('\r');
			}
			rd.close();

			statPrint(connection.getResponseCode(), response.toString());
			return response.toString();

		} catch (MalformedURLException ex) {

			statPrint("MalformedURLException", ex.getMessage());
			return GlobalVariables.CONN_EXCEPTION;

		} catch (java.net.ConnectException ex1) {
			statPrint("ConnectException", ex1.getMessage());
			return GlobalVariables.CONN_EXCEPTION;

		} catch (IOException ex2) {

			statPrint("IOException", ex2.getMessage());
			
			String problem = ex2.getClass().getName();
			
			if(GlobalMethods.validateString(problem))
			{
				if(problem.contains("java.net.SocketTimeoutException"))
				{
					return GlobalVariables.CONN_EXCEPTION;
				}
				else
				{
					return GlobalVariables.OTHER_EXCEPTION;
				}

			}
			else
			{
				return GlobalVariables.OTHER_EXCEPTION;
			}

		}
		catch(Exception ex3)
		{
			statPrint("Exception", ex3.getMessage());
			return GlobalVariables.OTHER_EXCEPTION;
		}

	}
	
	//open http upload stream to a destination
	//take actions according to network codes	
	public static ResponsePojo uploadRequestData(String jString, String targetURL) {

		URL url;
		HttpURLConnection connection = null;

		try {
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(jString.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setConnectTimeout(GlobalVariables.CONN_TIMEOUT);

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(jString);

			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			StringBuffer response = new StringBuffer();

			while ((line = rd.readLine()) != null) {
				response.append(line);
				//response.append('\r');
			}
			rd.close();

			statPrint(connection.getResponseCode(), response.toString());
			return new ResponsePojo(connection.getResponseCode(), response.toString());

		} catch (MalformedURLException ex1) {

			statPrint("MalformedURLException", ex1.getMessage());
			return new ResponsePojo(-1, GlobalVariables.CONN_EXCEPTION);

		} catch (java.net.ConnectException ex1) {

			statPrint("ConnectException", ex1.getMessage());
			return new ResponsePojo(-1, GlobalVariables.CONN_EXCEPTION);

		} catch (FileNotFoundException ex) {

			Log.e(TAG, ex.getMessage());
			try {

				statPrint(connection.getResponseCode(), ex.getMessage());
				return new ResponsePojo(connection.getResponseCode(), GlobalVariables.OTHER_EXCEPTION);

			} catch (IOException e) {

				return new ResponsePojo(-1, GlobalVariables.OTHER_EXCEPTION);
			}

		} catch (IOException ex2) {

			statPrint("IOException", ex2.getMessage());
			
			String problem = ex2.getClass().getName();
			
			if(GlobalMethods.validateString(problem))
			{
				if(problem.contains("java.net.SocketTimeoutException"))
				{
					return new ResponsePojo(413, GlobalVariables.CONN_EXCEPTION); // to trigger resize in image upload
				}
				else
				{
					return new ResponsePojo(-1, GlobalVariables.OTHER_EXCEPTION);
				}

			}
			else
			{
				return new ResponsePojo(-1, GlobalVariables.OTHER_EXCEPTION);
			}

		}
		catch(Exception ex3)
		{
			statPrint("Exception", ex3.getMessage());
			return new ResponsePojo(-1, GlobalVariables.OTHER_EXCEPTION);
		}

	}

	//print connection code with messages
	private static void statPrint(int code, String msg) {
		statPrint(String.valueOf(code), msg);
	}

	//print messages
	private static void statPrint(String code, String msg) {
		if(GlobalVariables.PRINT_DEBUG)
		{
			System.out.println(code+": "+msg);
		}
	}
	
}
