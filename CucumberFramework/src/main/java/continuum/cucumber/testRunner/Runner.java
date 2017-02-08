package continuum.cucumber.testRunner;

import org.junit.runner.RunWith;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;

@RunWith(Cucumber.class)
@CucumberOptions(
monochrome = true,
features = "src//test//resources//features",
glue="continuum.cucumber.stepDefinations",
plugin = {
"pretty",
"html:test-report/cucumber",
"json:test-report/cucumber.json",
"rerun:target/rerun.txt" }
)
public class Runner {
private TestNGCucumberRunner testNGCucumberRunner;
private static String featureName=null;
//static RemoteWebDriver driver=null;

@BeforeClass(alwaysRun = true)
public void setUpClass() throws Exception {
	
    testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
}



@Test(groups="cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
public void feature(CucumberFeatureWrapper cucumberFeature) {
	 
	this.featureName=cucumberFeature.getCucumberFeature().getPath().toString();  
}

@DataProvider
public Object[][] features() {
	
		   return testNGCucumberRunner.provideFeatures();
}



public static String getFeatureName(){
	return featureName;
}
}

