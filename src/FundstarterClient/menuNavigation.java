package FundstarterClient;

import java.util.Arrays;
import java.util.Scanner;
import FundstarterClient.inputCheck;

public class menuNavigation {
	
	public static void splashScreen() {
		System.out.println("    ______                __     __             __           ");
		System.out.println("   / ____/_  ______  ____/ /____/ /_____ ______/ /____  _____");
		System.out.println("  / /_  / / / / __ \\/ __  / ___/ __/ __ `/ ___/ __/ _ \\/ ___/");
		System.out.println(" / __/ / /_/ / / / / /_/ (__  ) /_/ /_/ / /  / /_/  __/ /    ");
		System.out.println("/_/    \\__,_/_/ /_/\\__,_/____/\\__/\\__,_/_/   \\__/\\___/_/");
		System.out.println("\n");
	}

	public static void mainMenu() {
		System.out.println("Choose an option:");
		System.out.println("\t1. Login/User Panel");
		System.out.println("\t2. Register");
		System.out.println("\t3. Show Projects");
		String[] ansArr = {"1","2","3"};
		int answer = inputCheck.getMenuAnswer(ansArr);
		
		switch (answer){
			case 1:	//Go to login menu
					break;
			case 2:	//Go to register function
					break;
			case 3:	//Go to Projects menu
					break;
			default:System.out.println("Err: Main Menu - Switch case not found for " + answer);
					break;
		}
		
	}
	
}
