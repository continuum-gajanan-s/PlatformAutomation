package continuum.cucumber.reporting;

import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;


/*********
 * Utility to to generate cucumber report
 * @author sneha.chemburkar
 *
 */
public class GenerateReport {

	
	
	public static void generateReport(){
		CucumberResultsOverview results = new CucumberResultsOverview();
		results.setOutputDirectory("test-report");
		results.setOutputName("cucumber-results");
		results.setSourceFile("test-report/cucumber.json");
		try {
			results.execute(false);
		} catch (Exception e) {
			System.out.println("Not able to create cucumber reports");
		}
		
	}
}

