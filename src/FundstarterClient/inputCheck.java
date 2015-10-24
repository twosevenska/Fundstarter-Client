package FundstarterClient;

import java.util.Arrays;
import java.util.Scanner;

public class inputCheck {
	public static int getMenuAnswer(String[] valStr){
		boolean valid = false;
	
		Scanner sc = new Scanner(System.in);
		while(!valid){
			String readval = sc.nextLine();
			if(Arrays.asList(valStr).contains(readval)){
				valid = true;
				sc.close();
				return Integer.parseInt(readval);
			}else{
				System.out.println("Invalid input: " + readval);
				System.out.println("Please try again.");
			}
		}
		sc.close();
		return 0;
	}
}
