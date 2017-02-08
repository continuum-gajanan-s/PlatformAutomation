/**
 * 
 */
package continuum.cucumber.webservices;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author sneha.chemburkar
 *
 */
public class RestServicesUtility {

	/**
	 * @param wsUrl
	 * @return status 
	 * get status of webservice
	 */
	public static int getStatusofWebService(String wsUrl){

		URL url;
		int code=0;
		try {
			url = new URL(wsUrl);
	
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		code = connection.getResponseCode();

		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return code;
	}
	
	
	/**
	 * @param url
	 * @return
	 * @throws Exception
	 * get response of web-service
	 */
	
	
	public static InputStream getResponseofWebService(String url) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		System.out.println("\nSending 'GET' request to URL : " + url);
		int responseCode = con.getResponseCode();
		InputStream  jsonInputStream=con.getInputStream();
		System.out.println("Response Code : " + responseCode);
  
		return jsonInputStream;

	}
	
	
//	public static String getResponseofWebServiceonRemoteMachine(Session session,String url) throws Exception {
//		  Channel channel;
//			try {
//				channel = session.openChannel("exec");
//			
//         
//          channel.setInputStream(null);
//          ((ChannelExec)channel).setErrStream(System.err);
//
//          InputStream in=channel.getInputStream();
//          channel.connect();
//		URL obj = new URL(url);
//		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//		con.setRequestMethod("GET");
//	
//		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'GET' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);
//
//		BufferedReader ini = new BufferedReader(
//		        new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while ((inputLine = ini.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();
//         System.out.println("Response "+response.toString());
//		//return result
//		return response.toString();
//
//	}
	/**
	 * @param url
	 * @param header
	 * @return
	 * @throws Exception
	 * get response of web-service by setting header parameters
	 */
	public static String getResponseofWebService(String url, HashMap<String,String> header) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		for(Map.Entry<String, String> prop : header.entrySet()) {
			con.setRequestProperty(prop.getKey(),prop.getValue());
		}
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//return result
		return response.toString();

	}
	
	/**
	 * @param url
	 * @param postParameters
	 * @param header
	 * @return response string
	 * post response of web-service along with post parameters
	 */
	public static String postRequest(String url, String postParameters,HashMap<String,String> header)
	{
		
		HttpsURLConnection con;
		String inputLine;
		StringBuffer response=null;
		
		try {
			URL obj = new URL(url);
			con = (HttpsURLConnection) obj.openConnection();
			
		con.setRequestMethod("POST");
			
		for(Map.Entry<String, String> prop : header.entrySet()) {
			con.setRequestProperty(prop.getKey(),prop.getValue());
		}
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + postParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		
		response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		//print result
		System.out.println(response.toString());
           return response.toString();
	}
	
	public static void main(String []args){
		String url="http://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway";
		try {
			getResponseofWebService(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
