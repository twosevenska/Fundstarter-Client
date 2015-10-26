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
	
	private static Com_object createTcpSocket(int userId, operationtype operation, Hashtable<String, String> content) {
		Socket objsock = null;
		int serversocket = 6000;
		
		try {
			// First open the socket
			objsock = new Socket("localhost", serversocket);
			// For debugging purposes
			
			System.out.println("SOCKET=" + objsock);
			
			// Now open the object streams
			ObjectOutputStream oos = new ObjectOutputStream(objsock.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(objsock.getInputStream());
			// Let's create the object
			Com_object comOut = new Com_object(userId, operation, content);
			// And now send it
			oos.writeObject(comOut);
			// Better clean the socket
			oos.flush();
			// And now read
			Com_object comIn = (Com_object) ois.readObject();
			return comIn;
		} catch (UnknownHostException e){System.out.println("Sock:" + e.getMessage());
		} catch (IOException e) {System.out.println("IO:" + e.getMessage());
		} catch (ClassNotFoundException e) {System.out.println("Wrong Object:" + e.getMessage());
		} finally{
			if (objsock != null)
				try {
					objsock.close();
				} catch (IOException e) {
				    System.out.println("close:" + e.getMessage());
				}
		}
		return null;
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
		Com_object comIn = createTcpSocket(userId, operationtype.login, loginData);
		
		if(verbose)
			System.out.println("TEST@loginUser: Now getting userID");
		
		userId = Integer.parseInt(comIn.elements.get("userid"));
		
		if(verbose)
			System.out.println("TEST@loginUser: Got userID = "+ userId);
		
		return userId;
	}
	
	
	
}
