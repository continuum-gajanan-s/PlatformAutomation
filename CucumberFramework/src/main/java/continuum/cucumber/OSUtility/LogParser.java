package continuum.cucumber.OSUtility;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;



/***************
 * Utility to parse text log files
 * @author abhishek
 *
 */	

public class LogParser {
	

		private static final String DATE_FORMAT = "M/d/yyyy";

		public static boolean isTextPresent(String filePath, String text) throws IOException {
			return isTextPresent(filePath, (Date) null, text);
		}

		public static boolean isTextPresent(String filePath, Date date, String text) throws IOException {

			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			String formattedDate = null;
			if (date != null) {
				formattedDate = sdf.format(date);
			}

			return isTextPresent(filePath, formattedDate, text);
		}

		public static boolean isTextPresent(String filePath, String formattedDate, String text) throws IOException {
			String line;
			BufferedReader buffer = null;
			try {
				buffer = new BufferedReader(new FileReader(filePath));
				while ((line = buffer.readLine()) != null) {
					if (formattedDate != null && !line.contains(formattedDate)) {
						continue;
					} else if (line.contains(text)) {
						return true;
					}
				}
			} finally {
				IOUtils.closeQuietly(buffer);
			}
			return false;
		}

		public static List<String> getValues(String filePath, String key) throws IOException {
			return getValues(filePath, (Date) null, key);
		}

		public static List<String> getValues(String filePath, Date date, String key) throws IOException {

			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			String formattedDate = null;
			if (date != null) {
				formattedDate = sdf.format(date);
			}

			return getValues(filePath, formattedDate, key);
		}

		public static List<String> getValues(String filePath, String formattedDate, String key) throws IOException {
			List<String> list = new ArrayList<String>();
			FileReader file = new FileReader(filePath);
			String line;
			BufferedReader buffer = null;
			try {
				buffer = new BufferedReader(file);
				while ((line = buffer.readLine()) != null) {
					if (formattedDate != null && !line.contains(formattedDate)) {
						continue;
					}
					if (line.contains(key)) {
						list.add(line);
					}
				}
			} finally {
				IOUtils.closeQuietly(buffer);
			}
			return list;
		}
	

}
