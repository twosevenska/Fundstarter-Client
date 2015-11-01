package fundstarterClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import fundstarterClient.menuNavigation;


/**
 * @author Goncalo Lacerda Campos de Sao Pedro Barroso N2010134499
 * @author Flavio Miguel Luis Penas N2011171449
 */
public class Main {
	
	public static boolean verbose = false;
	public static String serverAlphaAddress = "192.168.1.76";
	public static String serverBetaAddress = "localhost";
	public static String serverAlphaPort = "6000";
	public static String serverBetaPort = "7000";
	
	/**
	 * Setups global environments and calls the main menu.
	 * @param args The program can take either 0 or 5 arguments. They are in order: String serverAlphaAddress, String serverBetaAddress, String serverPortAddress and boolean verbose.
	 */
	public static void main(String[] args) {
		boolean conSuc;
		
		if (args.length == 0){
			loadProperties();
		}else if(args.length == 5){
			loadArguments(args);
		}else{
			System.out.println("Invalid number of arguments, got " + args.length + " expected 0 or 5" );
		}
		
		System.out.println("Connecting to server. Please wait.");
		
		conSuc = tcpClient.createTcpSocket();
		
		if(conSuc){
			while(true){
				menuNavigation.splashScreen();
				menuNavigation.mainMenu();
			}
		}else{
			System.out.println("We're sorry but we couldn't connect to the server.");
			System.out.println("Please try again later.");
		}
	}
	
	/**
	 * Tries to load the global variables from a properties file
	 */
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
			boolean tempVerb = "true".equals(prop.getProperty("verbose"));
			String tempPort1 = prop.getProperty("serverPort1");
			String tempPort2 = prop.getProperty("serverPort2");
			
			if(valid){

				if(tempServer1 != null)
					serverAlphaAddress = tempServer1;
				
				if(tempServer2 != null)
					serverBetaAddress = tempServer2;
				
				if(tempPort1 != null)
					serverAlphaPort = tempPort1;
				
				if(tempPort2 != null)
					serverBetaPort = tempPort2;
				
				if(tempVerb){
					verbose = true;
				}else{
					verbose = false;
				}
			}
			
			if(verbose){
				System.out.println("Printing contents of properties file.");
				System.out.println("server1 " + tempServer1);
				System.out.println("server2 " + tempServer2);
				System.out.println("verbose " + tempVerb);
				System.out.println("serverPort1 " + tempPort1);
				System.out.println("serverPort2 " + tempPort2);
			}

		} catch (IOException ex) {
			if(verbose){
				System.out.println("Could not get properties file.");
				ex.printStackTrace();
			}
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					if(verbose)
						e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Expects the list of arguments gotten from main and sets up the global variables
	 * 
	 * @param arguments Expects a list of arguments
	 */
	private static void loadArguments(String[] arguments){
		serverAlphaAddress = arguments[0];
		serverBetaAddress = arguments[2];
		serverAlphaPort = arguments[1];
		serverBetaPort = arguments[3];
		if(arguments[4].equalsIgnoreCase("true")){
			verbose = true;
		}else if(arguments[4].equalsIgnoreCase("false")){
			verbose = false;
		}else{
			System.out.println("Invalid fifth argument, got " + arguments[4] + " expected a boolean." );
		}
	}

}

