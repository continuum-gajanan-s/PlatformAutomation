package continuum.cucumber.OSUtility;

import java.util.List;

import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;

/**
 * This contains OS independent utility methods to retrieve process list, find
 * if process is running etc.
 * author visha;l
  *
 */
public class ProcessUtility {

	/**
	 * Checks if a process with specified name is running or not.
	 * <p>
	 * <b>NOTE: </b>If a process running is firefox.exe, then it will return
	 * true for {@code processName} as firefox.exe or firefox or fire
	 * </p>
	 * 
	 * @param processName full or partial name of process to check
	 * @return
	 */
	public static boolean isProcessRunning(String processName) {
		return (getProcessList(processName).size() > 0);
	}

	/**
	 * Method to get OS process list. Like in windows, it will return all
	 * processes as listed in task manager
	 * 
	 * @return
	 */
	public static List<ProcessInfo> getProcessList() {
		return JProcesses.getProcessList();
	}

	/**
	 * Method to get OS process list with specified process name.
	 * @param processName
	 * @return
	 */
	public static List<ProcessInfo> getProcessList(String processName) {
		return JProcesses.getProcessList(processName);
	}
}
