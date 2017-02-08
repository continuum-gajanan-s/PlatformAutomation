/**
 * 
 */
package continuum.cucumber.OSUtility;
import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author sneha.chemburkar
 *
 */
public class RemoteSSHConnection {



	 public static Channel createSSHConnection(String host,int port, String userName, String password) {
//	  String host = "10.2.42.148";
//	  String user = "junovm";
//	  String password = "junovm@123";
//	  String command1 ="ls -l";
		 Session session=null;
		 Channel channel=null;
	  try{
	  
	   JSch jsch = new JSch();
	   session=jsch.getSession(userName, host, port);
	            session.setPassword(password);
	            session.setConfig("StrictHostKeyChecking", "no");
	            session.connect();
	            System.out.println("Remote machine Connected");

	           
				channel = session.openChannel("exec");
				
	            
	            channel.setInputStream(null);
	            ((ChannelExec)channel).setErrStream(System.err);

	        
				} catch (JSchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();}
            return channel;
	  }
	  
	  public static void executeCommand(Channel channel,String command) {
		  ((ChannelExec)channel).setCommand(command);
		
			
//	            Channel channel;
//				try {
//					channel = session.openChannel("exec");
//				
//	            ((ChannelExec)channel).setCommand(command);
//	            channel.setInputStream(null);
//	            ((ChannelExec)channel).setErrStream(System.err);
		        ((ChannelExec)channel).setCommand(command);
		     try {
				channel.connect();
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//	            InputStream in=channel.getInputStream();
//	          
//	            byte[] tmp=new byte[1024];
//	            while(true){
//	              while(in.available()>0){
//	                int i=in.read(tmp, 0, 1024);
//	                if(i<0)break;
//	                System.out.print(new String(tmp, 0, i));
//	              }
//	            }      
	            System.out.println(channel.getExitStatus());
	   	   channel.disconnect();
				
	 }
	  
				
				public static  void closeSSHConnection(Channel channel)
				{
					if(channel.isClosed()){
		                System.out.println("exit-status: "+channel.getExitStatus());
		                
		              }
		              try{Thread.sleep(1000);}catch(Exception ee){
		            	  System.out.println("Execetion "+ee.getMessage());
		              }
		            
		            channel.disconnect();
		            try {
						channel.getSession().disconnect();
					} catch (JSchException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            System.out.println("Connection closed");
				}
//		public static void executeCommandOnWindowsMachine(String machine, String username, String password,String command) {
//			try {
//				String executeCmd = "cmd /c net use \\\\" + machine + "\\IPC$ /U:" + username + " " + password;
//				Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
//				System.out.println("Execution output"+runtimeProcess.getOutputStream().);
//				int cmdStatus = runtimeProcess.waitFor();
//				System.out.println("Execution status"+cmdStatus);
//			} catch (IOException | InterruptedException ex) {
//				System.out.println("Error to connect to windows machine: "+ ex);
//			}
//		}
		
			public static void executeCommandOnWindows(String machine, String username, String password, String executeCmd ){	
				System.out.println("Execute command: " + executeCmd);
				try {
				Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
				 InputStream in=runtimeProcess.getInputStream();
		           
		            byte[] tmp=new byte[1024];
		            while(true){
		              while(in.available()>0){
		                int i=in.read(tmp, 0, 1024);
		                if(i<0)break;
		                System.out.print(new String(tmp, 0, i));
		              }
		            
				
		            }
		           
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
			}
			
		

//	public static void main(String []args)
//	{
//		String osType="Windows";
//		if(osType.equalsIgnoreCase("Linux"))
//		{
//		Session sh= createSSHConnection("10.2.42.148",22,"junovm","junovm@123");
//		//executeCommand(sh,"sudo visudo");
//		executeCommand(sh,"service cassandra status");
//		}
//		else if(osType.equalsIgnoreCase("Windows"))
//		{
//			//Session sh= createSSHConnection("10.2.14.207",22,"mahesh.mahajan","MAhi1234@");
//			executeCommandOnWindows("10.2.14.207","mahesh.mahajan","MAhi1234@","cmd.exe /c dir");
//		}
//	}
	
	
}
