package continuum.cucumber.testRunner;
import org.junit.runner.RunWith;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import continuum.cucumber.reporting.GenerateReport;
import continuum.cucumber.reporting.HtmlEmailSender;
import continuum.cucumber.reporting.TestRailIntegrator;
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
"rerun:target/rerun.txt" },
tags={"@Test"}
)
public class TestRunner {
private TestNGCucumberRunner testNGCucumberRunner;
private static String scenarioName=null;
static RemoteWebDriver driver=null;

@BeforeClass(alwaysRun = true)
public void setUpClass() throws Exception {
    testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
}

@BeforeTest(alwaysRun = true)
  public void beforeTest()
  {
// String browserName= Utilities.getMavenProperties("browser").toUpperCase();
//  driver=WebDriverInitialization.createInstance(driver,browserName);
//   
//    DriverFactory.setWebDriver(driver);

  }

@Test(groups="cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
public void feature(CucumberFeatureWrapper cucumberFeature) {
 
 scenarioName=cucumberFeature.getCucumberFeature().getPath();
 
 System.out.println("**************Executing scenario *********"+scenarioName);
// System.out.println("**************Executing scenario *********"+cucumberFeature.getCucumberFeature().getPath());
    testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
   
}

@DataProvider
public Object[][] features() {
 
     return testNGCucumberRunner.provideFeatures();
}

@AfterTest(alwaysRun = true)
public void afterTest(){
 
}


@AfterClass(alwaysRun = true)
public void tearDownClass() throws Exception {
    testNGCucumberRunner.finish();
  GenerateReport.generateReport();
  HtmlEmailSender.sendReport();
  
  TestRailIntegrator.updateResultToTestRail();
}

public static String getScenarioName(){
 return scenarioName;
}
}