package continuum.cucumber.reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CucumberJsonResult {


	public final static String reportPath = new File("").getAbsolutePath()+"\\test-report\\cucumber.json";

	public static Map<String, String> readJson() {

	//public static void main(String[] args)  {
		Map<String,String> scenarioResults = new HashMap<>();

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArrScenario,jsonArrSteps=null;
			Iterator iterator=null;
			String result = null,combineString=null;
			JSONObject jsonObj,jsonstepObj,jsonstepResult,jsonscenario = null;

			Object obj = parser.parse(new FileReader(reportPath));
			

			JSONArray jsonArrFeature = (JSONArray) obj;
			Iterator iteratorFeature = jsonArrFeature.iterator();
			while (iteratorFeature.hasNext()) 
			{
				jsonObj = (JSONObject) iteratorFeature.next();
				String featurename=jsonObj.get("name").toString();
				System.out.println("Feature name: "+featurename);



				jsonArrScenario = (JSONArray) jsonObj.get("elements") ;
				Iterator itr = jsonArrScenario.iterator();
				while (itr.hasNext()) 

				{

					jsonscenario =  (JSONObject) itr.next();   
                    if(jsonscenario.get("keyword").toString().equalsIgnoreCase("Background"))
                    {
                    	jsonscenario =  (JSONObject) itr.next(); 
                    }

					jsonArrSteps = (JSONArray) jsonscenario.get("steps") ;

					iterator = jsonArrSteps.iterator();
					long duration=0;
                    String errorMsg="Executed via automation";
					while (iterator.hasNext()) {
						jsonstepObj = (JSONObject)iterator.next();
						jsonstepResult = (JSONObject) jsonstepObj.get("result");
						result=jsonstepResult.get("status").toString();
						duration=duration+Long.valueOf(jsonstepResult.get("duration").toString());
						if(result.equalsIgnoreCase("failed")||result.equalsIgnoreCase("skipped"))
						{
							errorMsg=jsonstepResult.get("error_message").toString().substring(0,300);
							break;
						}

					}
					combineString=result+"_"+duration+"_"+errorMsg;
					scenarioResults.put((String) jsonscenario.get("name"),combineString);		
				}
			}
			//printResult(scenarioResults);

		}catch( Exception e){
			System.out.println("Unable to parse cucumber.json result file");
			e.printStackTrace();
		}
		return scenarioResults;
	}


//	public static void main(String []args ){
//		Map<String, String> scenarioResults=readJson();
//		for (Map.Entry<String, String> scenario : scenarioResults.entrySet()) {
//			System.out.println("*********Scenario :"+scenario.getKey()+" has  "+scenario.getValue()+"********");
//		}
//	}
}

