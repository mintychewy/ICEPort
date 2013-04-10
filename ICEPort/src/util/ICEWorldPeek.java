package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ICEWorldPeek {

	final static String BASE_URL = "http://iceworld.sls-atl.com/api&cmd=";
	static String output = "";
	static String output2 = "";
	@SuppressWarnings("finally")
	public static String getLooks(String uid) throws Exception {
	
		URL url = null;
		try {
			url = new URL("http://iceworld.sls-atl.com/api&cmd=gresources&uid="+uid);
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
		}

		/**
		 * Open connection. GET and print out the message
		 */
		try {
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");

			if (connection.getResponseCode() != 200) {
				System.out.println("Failed : HTTP error code : "
						+ connection.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
 
			String buffer = "";
			while ((buffer = br.readLine()) != null) {
				if(!buffer.equals(null) )
					output2 = buffer;
			}

			connection.disconnect();

		} catch (IOException e) {
			System.out.println("Connection failed");
		} finally{
			return output2;
		}
	}
	public static String getData(String cmd) throws Exception{

		/**
		 * Forms a URL object for HTTP request by appending base url with input command
		 */
		URL url = null;
		try {
			url = new URL(BASE_URL + cmd);
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
		}

		/**
		 * Open connection. GET and print out the message
		 */
		try {
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");

			if (connection.getResponseCode() != 200) {
				System.out.println("Failed : HTTP error code : "
						+ connection.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String buffer = "";
			while ((buffer = br.readLine()) != null) {
				if(!buffer.equals(null) )
					output += buffer;
			}

			connection.disconnect();

		} catch (IOException e) {
			System.out.println("Connection failed");
		} finally{
			return output;
		}


	}

	/**
	 * Checks whether if a URL is reachable
	 * 
	 * @param targetUrl
	 *            is the URL to be checked
	 * @return true if the URL is reachable and false otherwise
	 */
	public static boolean isReachable(String targetUrl) {
		HttpURLConnection httpUrlConn;
		try {
			httpUrlConn = (HttpURLConnection) new URL(targetUrl)
			.openConnection();

			// A HEAD request is just like a GET request, except that it asks
			// the server to return the response headers only, and not the
			// actual resource (i.e. no message body).
			// This is useful to check characteristics of a resource without
			// actually downloading it,thus saving bandwidth. Use HEAD when
			// you don't actually need a file's contents.
			httpUrlConn.setRequestMethod("HEAD");

			// Set timeouts in milliseconds
			httpUrlConn.setConnectTimeout(10000);
			httpUrlConn.setReadTimeout(10000);

			return (httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}

	}
}
