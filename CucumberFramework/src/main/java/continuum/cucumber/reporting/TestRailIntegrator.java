package continuum.cucumber.reporting;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.testng.Assert;

import continuum.cucumber.Utilities;
/*********
 * Utility to write results to test rail
 * @author sneha.chemburkar
 *
 */
public class TestRailIntegrator {
	static TestRailAPIClient client;

	/**
	 * method to update results to test rail
	 */
	public static void updateResultToTestRail(){
		int result=0;
		Long duration=null;
		String errorMsg=null;
		if(Utilities.getMavenProperties("TestRailUpdateFlag").equalsIgnoreCase("true"))
		{	
			
		connectToTestRail();
		String testRun=Utilities.getMavenProperties("TestRun");
//		String testRun="R1252";
		if(testRun.contains("R"))
		           testRun=testRun.split("R")[1];
		
		Map<String, String> scenarioResults=CucumberJsonResult.readJson();
		if(!scenarioResults.isEmpty())
		{
		  for(Map.Entry<String, String> scenario : scenarioResults.entrySet()) {
			  result=getTestRailStatus(scenario.getValue());
			   duration=getDuration(scenario.getValue());
			   errorMsg=getErrorMsg(scenario.getValue()); 
			//   System.out.println("*********Scenario :"+scenario.getKey()+" is executed in "+duration +"sec resulted in "+result);
			   if(scenario.getKey().contains(","))
			   {
				   List<String>tcId=getMultipleTcIDFromScenario(scenario.getKey());
				  
				   for(int i=0;i<tcId.size();i++)
				   {
					   addResultTestRail(testRun,tcId.get(i),result,duration,errorMsg); 
				   }
			   }
			   else
			       addResultTestRail(testRun,getTcIDFromScenario(scenario.getKey()),result,duration,errorMsg);
		     }
		}
		else
			System.out.println("*****Results not updated in Test Rail  ***** ");
	  }
	}

	
	private static String getErrorMsg(String result) {
		String errormsg=result.split("_")[2];
		return errormsg;
		
	}


	private static long getDuration(String result) {
		String duration=result.split("_")[1];
		long durSec=Long.valueOf(duration)/1000000000;

	
		//System.out.println("total execution time "+durSec);
		return durSec;
		
		
	}


	public static void main(String []args)
	{
		
	//	System.out.println("Connection with Test rail https://continuum.testrail.net");
		client = new TestRailAPIClient("https://continuum.testrail.net");
		client.setUser("qe_automation@continuum.net");
		client.setPassword("q34ut0m4t!0n");
	   updateResultToTestRail();
		
		
	}
	
	
	public static int getTestRailStatus(String result){
		String status=result.split("_")[0];
	    int testRailStatus=0;
		switch(status){
        case "passed":
        	testRailStatus = 1;
        	break;
        case "failed":
        	testRailStatus = 5;
        	break;
        case "skipped":
        	testRailStatus = 2;
        	break;
        }
		return testRailStatus;
	}
	
	/**
	 * method to connect to test rail
	 */
	public static void connectToTestRail(){
		
		client = new TestRailAPIClient(Utilities.getMavenProperties("TestRailUrl"));
		client.setUser(Utilities.getMavenProperties("TestRailUserName"));
		client.setPassword(Utilities.getMavenProperties("TestRailPassword"));
	}
	
	public static void addResultTestRail( String testRunID,String testCaseID,int status, Long duration ,String errorMsg)  {
		try {
			JSONObject obj = (JSONObject) client.sendGet("get_case/"
					+ Integer.parseInt(testCaseID));
			Map<String, Comparable> data = new HashMap<String, Comparable>();

			data.put("status_id", status);
			data.put("elapsed",duration+"s");
			data.put("comment", errorMsg);
			JSONObject r = (JSONObject) client.sendPost("add_result_for_case/"+
					testRunID + "/" + testCaseID, data);
			System.out.println("*******Result updated in  Test Rail  Successfully   ***************************");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TestRailAPIException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param scenario
	 * @return test case id
	 * it fetchs test case id from Test Rail
	 */
	public static String getTcIDFromScenario(String scenario){
		if(scenario.contains("-"))
		{
			 
		String tcID=scenario.split("-")[1];
		
		if(tcID.contains("C"))
			tcID=tcID.split("C")[1];
		return tcID;
		}
		else
			 Assert.fail("*******Test case id is not mentioned against Scenario***********");
	     return null;	  
	}
	
	
	public static List<String> getMultipleTcIDFromScenario(String scenario){
		List<String> tc = new ArrayList<String>(); 
	  if(scenario.contains("-"))
		{
			 
		String tcID=scenario.split("-")[1];
		System.out.println("Test case ids:"+tcID);
		if(tcID.contains(","))
		{
		
			for (String str: tcID.split(",")) 
		    {
		      if(str.contains("C"))
			  tc.add(str.split("C")[1]);
		    }
		    
		 }
		
		return tc;
		}
		else
			 Assert.fail("Test case id is not mentioned against Scenario");
	     return null;	  
	}
	




}

	

