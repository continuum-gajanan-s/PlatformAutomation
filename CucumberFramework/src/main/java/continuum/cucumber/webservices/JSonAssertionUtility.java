package continuum.cucumber.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.jayway.jsonpath.JsonPath;

public class JSonAssertionUtility {
	
	public static  void validateJsonContent(InputStream json, String jsonPath, String validationString){
		try {
			String jsonContent=JsonPath.read(json, jsonPath).toString();
			
			if(jsonContent.equalsIgnoreCase(validationString))
			{
				System.out.println("Json content validated at"+jsonPath);
			}
			else
				System.out.println("Json content not validated at "+jsonPath+" validation String "+validationString);	
			
		} catch (IOException e) {
			System.out.println("Invalid Jason or Json doesn't exists at jsonPath "+jsonPath);
			e.printStackTrace();
		}
		
	}
	
	public static  void validateJsonContent(Object json, String jsonPath, String validationString){
		ArrayList<String> jsonArray=JsonPath.read(json, jsonPath);
		String jsonContent=jsonArray.get(0);
		
		
		if(jsonContent.equalsIgnoreCase(validationString))
		{
			System.out.println("Json content validated at"+jsonPath);
		}
		else
			System.out.println("Json content not validated at "+jsonPath+" validation String "+validationString);
		
	}

}
