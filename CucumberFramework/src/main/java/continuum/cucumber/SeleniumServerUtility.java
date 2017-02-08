package continuum.cucumber;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;

public class SeleniumServerUtility {
	public static String absolutePath=new File("").getAbsolutePath();
	public static String IEDriverLocation=absolutePath+"\\drivers\\IEDriverServer.exe";
	public static String ChromeDriverLocation=absolutePath+"\\drivers\\chromedriver.exe";
	public static String serverLocation=absolutePath+"\\seleniumserver\\selenium-server-standalone-2.53.1.jar";

	
	public static void startServer(){
	 try 
		{
			
	    if(!getSeleniumServerStatus())	
    	{
	       System.out.println("******Starting selenium server *********");
            String command="cmd /c java -jar "+serverLocation+" -Dwebdriver.ie.driver="+IEDriverLocation+ " -Dwebdriver.chrome.driver="+ChromeDriverLocation;
	     // System.out.println("Start server command "+command);
	       
          Runtime.getRuntime().exec(command);
         
	       	
    	}  
	    Thread.sleep(2000);
		
		       
		        
		} catch (Exception e) {
			System.out.println("Not able to start selenium server");
			e.printStackTrace();	
		}
		
		
}
		
		static boolean getSeleniumServerStatus(){
		  boolean status=false;
		  try {

				//String huburl=Utilities.getMavenProperties("hubUrl")+"/status";
			  String huburl="http://127.0.0.1:4444/wd/hub/status";
				URL url = new URL(huburl);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();

				int code = connection.getResponseCode();

				if (code == 200) {
					status=true;
				//System.out.println("Selenium server is already running "+status);
			
				}
				connection.disconnect();

			  } catch (ClientProtocolException e) {

			//  System.out.println("Selenium server is not started yet");

			  } catch (IOException e) {

				//  System.out.println("Selenium server is not started yet");
			  }

			
		//	 System.out.println("String status:" + status);
			return status;
		}
		
		

		
		public static void killSeleniumServer() {
          try{

			Runtime.getRuntime().exec("cmd /c http://localhost:4444/selenium-server/driver/?cmd=shutDownSeleniumServer");

			} catch (IOException e) {
				System.out.println("Not able to kill selenium server");
				e.printStackTrace();
			}
	
     }
		
		public static void main(String args[]){
			startServer();
			killSeleniumServer();
		}

}



