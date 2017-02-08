package continuum.cucumber.OSUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.testng.Reporter;

public class WindowsCommandUtility {


	/**
	 * Connects to IPC share of remote machine. Should be called before
	 * executing any command on remote machine
	 * 
	 * @param machine remote machine name or IP
	 * @param username user on remote machine
	 * @param password password for user on remote machine
	 */
	public static void connectForIPCShare(String machine, String username, String password) {
		try {
			String executeCmd = "cmd /c net use \\\\" + machine + "\\IPC$ /U:" + username + " " + password;
			Reporter.log("DEV DEBUG: " + executeCmd);
			Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int cmdStatus = runtimeProcess.waitFor();
		} catch (IOException | InterruptedException ex) {
			Reporter.log("Error to connect to windows machine: "+ ex);
		}
	}

	/**
	 * Disconnects from IPC share of remote machine. Should be called after
	 * commands has been executed
	 * 
	 * @param serverName
	 */
	public static void disconnectIPCShare(String serverName) {
		try {
			String executeCmd = "cmd /c net use /delete \\\\" + serverName + "\\IPC$";
			Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int cmdStatus = runtimeProcess.waitFor();
		} catch (IOException | InterruptedException ex) {
			Reporter.log("Error to disconect connection to windows machine: "+ ex);
		}
	}

	/**
	 * This method is responsible to run locally command on windows machine
	 * 
	 * @param command
	 * @return command execution result
	 */
	public static String runCMDCommandLocally(String command) {
		String line;
		String result = "";
		String executeCmd = "cmd /c" + " " + command;
		int processComplete = 0;
		Process runtimeProcess;
		try {
			runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			TimeUnit.SECONDS.sleep(60);
			processComplete = runtimeProcess.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(runtimeProcess.getInputStream()));
			line = reader.readLine();
			while (line != null) {
				if (result.compareTo("") == 0)
					result = line;
				else
					result = result + "\n" + line;
				line = reader.readLine();
			}
		} catch (IOException | InterruptedException e) {
			Reporter.log("Error to execute command on windows: "+e);
		}
		if (processComplete == 0 || processComplete != 1056 || processComplete != 3240) {
			Reporter.log("It was not possible to execute command on Windows or exit code is unexpected: "
					+ processComplete);
		}
		return result;
	}
}
