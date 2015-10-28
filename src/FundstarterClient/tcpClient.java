package FundstarterClient;

import java.net.*;
import java.util.Hashtable;
import java.io.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import globalClasses.Com_object;
import globalClasses.Com_object.operationtype;

public class tcpClient {
	
	static boolean verbose = true;
	static Socket objsock = null;
	static ObjectOutputStream oos;
	static ObjectInputStream ois;
	
	public static void createTcpSocket() {
		
		int serversocket = 6000;
		
		try {
			// First open the socket
			objsock = new Socket("localhost", serversocket);
			if(verbose)
				System.out.println("SOCKET=" + objsock);
			
			// Now open the object streams
			oos = new ObjectOutputStream(objsock.getOutputStream());
			ois = new ObjectInputStream(objsock.getInputStream());
		} catch (UnknownHostException e){System.out.println("Sock:" + e.getMessage());
		} catch (IOException e) {System.out.println("IO:" + e.getMessage());
		} 
	}
	
	public static void closeTcpSocket(){
		if (objsock != null)
			try {
				objsock.close();
			} catch (IOException e) {
			    System.out.println("close:" + e.getMessage());
			}
	}
	
	private static Com_object sendThroughSocket(int userId, operationtype operation, Hashtable<String, String> content){
		Com_object comIn = null;
		// Let's create the object
		Com_object comOut = new Com_object(userId, operation, content);
		// And now send it
		try{
			oos.writeObject(comOut);
			// Better clean the socket
			oos.flush();
			// And now read
			comIn = (Com_object) ois.readObject();
		} catch (IOException e) {System.out.println("IO:" + e.getMessage());
		} catch (ClassNotFoundException e) {System.out.println("Wrong Object:" + e.getMessage());
		}
		
		return comIn;
	}
	
	public static int loginUser(String username, String password){
		int userId = 0;
		Hashtable<String, String> loginData = new Hashtable<String, String>();
		
		loginData.put("username", username);
		loginData.put("password", password);
		
		if(verbose){
			System.out.println("TEST@loginUser: Sending hash = " );
			System.out.println(loginData);
			System.out.println("TEST@loginUser: Sending everything now.");
		}
		Com_object comIn = sendThroughSocket(userId, operationtype.login, loginData);
		
		if(verbose)
			System.out.println("TEST@loginUser: Now getting userID");
		
		userId = Integer.parseInt(comIn.elements.get("userid"));
		
		if(verbose)
			System.out.println("TEST@loginUser: Got userID = "+ userId);
		
		return userId;
	}
	
	public static boolean registerUser(String username, String password){
		int userId = 0;
		int iStatus;
		Hashtable<String, String> loginData = new Hashtable<String, String>();
		
		loginData.put("username", username);
		loginData.put("password", password);
		
		if(verbose){
			System.out.println("TEST@registerUser: Sending hash = " );
			System.out.println(loginData);
			System.out.println("TEST@registerUser: Sending everything now.");
		}
		Com_object comIn = sendThroughSocket(userId, operationtype.register, loginData);
		
		if(verbose){
			System.out.println("TEST@registerUser: Now getting success status");
			System.out.println("TEST@registerUser: Got the hastable: " + comIn.elements);
		}
		
		String status = comIn.elements.get("status");
		iStatus = Integer.parseInt(status);
		
		if(verbose)
			System.out.println("TEST@registerUser: Got iStatus = "+ iStatus);
		
		if(iStatus == 0)
			return true;
		return false;
	}
	
	
	public static String checkWallet(int userId){
		String walletAmmount = null;
		Hashtable<String, String> identification = new Hashtable<String, String>();
		
		identification.put("userId", Integer.toString(userId));
		
		if(verbose)
			System.out.println("TEST@getWallet: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.check_wallet, identification);
		
		if(verbose)
			System.out.println("TEST@getWallet: Now getting wallet ammount");
		
		walletAmmount = comIn.elements.get("wallet");
		
		if(verbose)
			System.out.println("TEST@getWallet: Got walletAmmount = "+ walletAmmount);
		
		return walletAmmount;
	}
	
	public static boolean addMoneyWallet(int userId, String money){
		int mStatus;
		Hashtable<String, String> moneyHash = new Hashtable<String, String>();
		
		moneyHash.put("userId", Integer.toString(userId));
		moneyHash.put("userId", money);
		
		if(verbose)
			System.out.println("TEST@addMoneyWallet: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.add_wallet, moneyHash);
		
		if(verbose)
			System.out.println("TEST@addMoneyWallet: Now getting wallet update confirmation");
		
		mStatus = Integer.parseInt(comIn.elements.get("money"));
		
		if(verbose)
			System.out.println("TEST@addMoneyWallet: Got mStatus = "+ mStatus);
		
		if(mStatus == 0)
			return true;
		return false;
	}
	
	public static boolean createProject(int userId, String projName, String description,
										String endDate, String reqAmmount){
		int status = 0;
		Hashtable<String, String> projectHash = new Hashtable<String, String>();
		
		projectHash.put("userId", Integer.toString(userId));
		projectHash.put("projName", projName);
		projectHash.put("description", description);
		projectHash.put("endDate", endDate);
		projectHash.put("reqAmmount", reqAmmount);
		
		if(verbose)
			System.out.println("TEST@createProject: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.create_proj, projectHash);
		
		if(verbose)
			System.out.println("TEST@createProject: Now getting project creation confirmation");
		
		status = Integer.parseInt(comIn.elements.get("projectStatus"));
		
		if(verbose)
			System.out.println("TEST@createProject: Got status = "+ status);
		
		if(status == 0)
			return true;
		return false;
	}
	
	public static boolean createTier(int userId, String description, String reqAmmount){
		int status = 0;
		Hashtable<String, String> tierHash = new Hashtable<String, String>();
		
		tierHash.put("userId", Integer.toString(userId));
		tierHash.put("description", description);
		tierHash.put("reqAmmount", reqAmmount);
		
		if(verbose)
		System.out.println("TEST@createTier: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.add_meta, tierHash);
		
		if(verbose)
		System.out.println("TEST@createTier: Now getting tier creation confirmation");
		
		status = Integer.parseInt(comIn.elements.get("tierStatus"));
		
		if(verbose)
		System.out.println("TEST@createTier: Got status = "+ status);
		
		if(status == 0)
			return true;
		return false;
	}
	
}
