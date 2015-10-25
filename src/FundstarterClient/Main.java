package FundstarterClient;

import FundstarterClient.tcpClient;
import FundstarterClient.menuNavigation;

public class Main {

	public static void main(String[] args) {
		/* TODO:
		 * 1. Test this
		 * 2. Adapt this so it asks the user for his details
		 * 3. Creates the object and sends it
		 * 4. Make it work on a thread
		*/
		while(true){
			menuNavigation.splashScreen();
			menuNavigation.mainMenu();
		}
		// So let's run the test
		//tcpClient.createTcpSocket();
	}

}

