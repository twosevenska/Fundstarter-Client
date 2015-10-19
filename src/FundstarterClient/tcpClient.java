package FundstarterClient;

import java.net.*;
import java.io.*;
import java.io.ObjectOutputStream;

public class tcpClient {
	public static void createTcpSocket() {
		/* TODO: 
	 	 * 1. Test
	 	 * 2. Refactor this and remove the test sections
		*/
		Socket objsock = null;
		int serversocket = 6000;
		
		try {
			// First open the socket
			objsock = new Socket("localhost", serversocket);
			// For debugging purposes
			
			System.out.println("SOCKET=" + objsock);
			
			// Now open object streams
			ObjectOutputStream oos = new ObjectOutputStream(objsock.getOutputStream());
			// Let's create the object
			loginInfo userpassobj = new loginInfo("Jimmy", "hots");
			// And now send it
			oos.writeObject(userpassobj);
			// Better clean the socket
			oos.flush();
		} catch (UnknownHostException e){System.out.println("Sock:" + e.getMessage());
		} catch (IOException e) {System.out.println("IO:" + e.getMessage());
		} finally{
			if (objsock != null)
				try {
					objsock.close();
				} catch (IOException e) {
				    System.out.println("close:" + e.getMessage());
				}
		}
	}
}
