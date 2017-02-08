package continuum.cucumber.OSUtility;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WatcherUtility {
	

	
	/**
	 * @param dirPath
	 * @param fileNameToBeLooked
	 * @param timeoutInSecs
	 * @return true if file is created
	 * else return false
	 */
	public static boolean watchFileCreation(String dirPath, String fileNameToBeLooked, int timeoutInSecs)
	{
		boolean flag = false;
		try{
		System.out.println("Watching directory..."+dirPath);
	
		WatchService dirWatcher = FileSystems.getDefault().newWatchService();
		Path dir = Paths.get(dirPath);
		dir.register(dirWatcher, StandardWatchEventKinds.ENTRY_CREATE);

		WatchKey key = dirWatcher.poll(timeoutInSecs, TimeUnit.SECONDS);
		if (key != null) {
			List<WatchEvent<?>> events = key.pollEvents();
			for (WatchEvent<?> event : events) {
				String filename = (event.context() != null) ? event.context().toString() : null;
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE && fileNameToBeLooked.equals(filename)) {
					flag = true;
					break;
				}
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	  
	  /**
	 * @param filePath
	 * @return
	 */
	public boolean watchFileModification(String filePath){
			
		  boolean flag=false;
			WatchService dirWatcher;
			try {
				dirWatcher = FileSystems.getDefault().newWatchService();
			
			  Path dir = Paths.get(filePath);
			 
				dir.register(dirWatcher, StandardWatchEventKinds.ENTRY_MODIFY);
			
			  WatchKey key = dirWatcher.take();
			
			  List<WatchEvent<?>> events = key.pollEvents();
			  for (WatchEvent event : events) {
			   if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
			     flag= true;
			   }
			   else
				   flag= false;
			  }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  return flag;
			
			  
	  }  
			  /**
			 * @param filePath
			 * @return
			 */
			public boolean watchFileDeletion(String filePath){
					
				  boolean flag=false;
					WatchService dirWatcher;
					try {
						dirWatcher = FileSystems.getDefault().newWatchService();
					
					  Path dir = Paths.get(filePath);
					 
						dir.register(dirWatcher, StandardWatchEventKinds.ENTRY_DELETE);
					
					  WatchKey key = dirWatcher.take();
					
					  List<WatchEvent<?>> events = key.pollEvents();
					  for (WatchEvent event : events) {
					   if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
					     flag= true;
					   }
					   else
						   flag= false;
					   }
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  return flag;
					
			  }

}
