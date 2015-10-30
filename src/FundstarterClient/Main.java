package FundstarterClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import FundstarterClient.menuNavigation;


public class Main {
	
	public static boolean verbose = true;
	public static String serverAlphaAddress = "localhost";
	public static String serverBetaAddress = "localhost";
	
	public static void main(String[] args) {
		if (args.length == 0){
			loadProperties();
		}else if(args.length == 3){
			loadArguments(args);
		}else{
			System.out.println("Invalid number of arguments, got " + args.length + " expected 0 or 3" );
		}
		
		tcpClient.createTcpSocket();
		
		while(true){
			menuNavigation.splashScreen();
			menuNavigation.mainMenu();
		}
	}
	
	private static void loadProperties(){
		Properties prop = new Properties();
		InputStream input = null;
		boolean valid = true;
		try {
			input = new FileInputStream("config.properties");

			// Load a properties file
			prop.load(input);

			// Get the properties, almost like a hash table
			String tempServer1 = prop.getProperty("server1");
			String tempServer2 = prop.getProperty("server2");
			String tempVerb = prop.getProperty("verbose");
			
			if(tempServer1 == null && tempServer2 == null && tempVerb == null)
				valid = false;
			
			if(valid){
				if(tempServer1 != null)
					serverAlphaAddress = tempServer1;
				
				if(tempServer2 != null)
					serverBetaAddress = tempServer2;
				
				if(tempVerb == "true" && tempVerb != null)
					verbose = true;
			}

		} catch (IOException ex) {
			//ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void loadArguments(String[] arguments){
		serverAlphaAddress = arguments[0];
		serverBetaAddress = arguments[1];
		if(arguments[2].equalsIgnoreCase("true")){
			verbose = true;
		}else if(arguments[2].equalsIgnoreCase("false")){
			verbose = false;
		}else{
			System.out.println("Invalid third argument, got " + arguments[2] + " expected a boolean." );
		}
	}

}

