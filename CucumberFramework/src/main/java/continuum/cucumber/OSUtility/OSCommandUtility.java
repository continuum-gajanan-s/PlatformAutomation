package continuum.cucumber.OSUtility;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.testng.internal.collections.Pair;

/**
 * Class to execute OS commands. This contains OS independent utility methods
 *
 *author vishal
 */
public class OSCommandUtility {

	/**
	 * Executes given OS command and returns command
	 * output(error or result).
	 * 
	 * @param command
	 * @return
	 */
	public static String executeCommand(String command) {
		return executeCommandReturnOutput(command, 60000).second();
	}

	/**
	 * Executes given OS command and returns command
	 * output(error or result).
	 * 
	 * @param command
	 * @param timeoutInSecs
	 * @return
	 */
	public static String executeCommand(String command, int timeoutInSecs) {
		return executeCommandReturnOutput(command, timeoutInSecs).second();
	}

	/**
	 * Executes given OS command and returns exit
	 * code
	 * 
	 * @param command
	 * @return
	 */
	public static int executeCommandAndGetExitCode(String command) {
		return executeCommandReturnOutput(command, 60000).first();
	}

	private static Pair<Integer, String> executeCommandReturnOutput(final String commandLine,
			final long timeout) {
		int exitVal = Integer.MIN_VALUE;
		Executor executor = new DefaultExecutor();
		ExecuteWatchdog watchdog = new ExecuteWatchdog(timeout);
		executor.setWatchdog(watchdog);
		ByteArrayOutputStream outAndErr = new ByteArrayOutputStream();
		try {
			PumpStreamHandler streamHandler = new PumpStreamHandler(outAndErr);
			executor.setStreamHandler(streamHandler);
			exitVal = executor.execute(CommandLine.parse(commandLine));
		} catch (final Exception e) {
		}

		Pair<Integer, String> exitCodeAndOutput = Pair.create(exitVal, outAndErr.toString());
		return exitCodeAndOutput;
	}
}
