package tw.waterdrop.lib;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSON{
	private String TAG = "JSON";
	public JSONObject json;
	public JSONObject getJSONFromURL(String... data) throws IOException {
		// URL to the JSON data
		// http://wptrafficanalyzer.in/p/demo1/first.php/countries
		
		try {
			json = new JSONObject(downloadUrl(data));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;

	}

	private String downloadUrl(String... strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		try {
			String urlString = "";
			for(int i=0;i<strUrl.length;i++)
			{
				urlString += strUrl[i] + "/";
			}
			Log.v(TAG,urlString);
			URL url = new URL(urlString);

			// Creating an http connection to communicate with url
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {

		} finally {
			iStream.close();
		}

		return data;
	}

}