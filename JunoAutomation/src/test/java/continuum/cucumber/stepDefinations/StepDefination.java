/**
 * 
 */
package continuum.cucumber.stepDefinations;

import java.io.InputStream;

import com.jcraft.jsch.Channel;

import continuum.cucumber.Utilities;
import continuum.cucumber.OSUtility.RemoteSSHConnection;
import continuum.cucumber.Page.JunoPageFactory;
import continuum.cucumber.webservices.JSonAssertionUtility;
import continuum.cucumber.webservices.RestServicesUtility;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * @author sneha.chemburkar
 *
 */
public class StepDefination extends JunoPageFactory {

	
	Channel ch;
	String osType=Utilities.getMavenProperties("OSType");
	InputStream json;
	
	@Given("^Start Remote connection$")
	public void start_Remote_connection() throws Throwable {
		ch= RemoteSSHConnection.createSSHConnection("10.2.42.148",22,"junovm","junovm@123");
	}

	@When("^Close Remote Connection$")
	public void close_Remote_Connection() throws Throwable {
		RemoteSSHConnection.closeSSHConnection(ch);
	}
	@Given("^Start platform services$")
	public void start_platform_services() {
		System.out.println("Start Platform Services");
		if(osType.equalsIgnoreCase("Linux"))
		{
		//RemoteSSHConnection.executeCommand(sh,"./platform-agent-core");
		
		RemoteSSHConnection.executeCommand(ch,"cat performance.log");
		
		}
	}

	@Given("^Start casandra server$")
	public void start_casandra_server() {
		System.out.println("Start Casandra server");
		if(osType.equalsIgnoreCase("Linux"))
		{
		RemoteSSHConnection.executeCommand( ch,"service cassandra status");
		}
	}

	@Then("^Call performance webservice$")
	public void call_performance_webservice() throws Throwable {
	 json= RestServicesUtility.getResponseofWebService("http://10.2.42.148:8082/performance/v1/endpoint/1/process");
	}

	@When("^Verify Performance data$")
	public void verify_Performance_data() {
		JSonAssertionUtility.validateJsonContent(json, "$.processID", "7757");
	}
	
	
	@Then("^Casandra is running$")
	public void casandra_is_running() throws Throwable {
		String osType=Utilities.getMavenProperties("OSType");
		if(osType.equalsIgnoreCase("Linux"))
		{
		Channel ch= RemoteSSHConnection.createSSHConnection("10.2.42.148",22,"junovm","junovm@123");
		//executeCommand(sh,"sudo visudo");
		RemoteSSHConnection.executeCommand(ch,"service cassandra status");
		}
		else if(osType.equalsIgnoreCase("Windows"))
		{
			//Session sh= createSSHConnection("10.2.14.207",22,"mahesh.mahajan","MAhi1234@");
			RemoteSSHConnection.executeCommandOnWindows("10.2.14.207","mahesh.mahajan","MAhi1234@","cmd.exe /c dir");
		}
	}		
}
