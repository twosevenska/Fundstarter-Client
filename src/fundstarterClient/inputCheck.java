package fundstarterClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class inputCheck {
	
	public static int getMenuAnswer(String[] valStr){
		boolean valid = false;
	
		Scanner sc = new Scanner(System.in);
		while(!valid){
			String readval = sc.nextLine();
			if(Arrays.asList(valStr).contains(readval)){
				valid = true;
				////sc.close();
				return Integer.parseInt(readval);
			}else{
				System.out.println("Invalid input: " + readval);
				System.out.println("Please try again.");
			}
		}
		////sc.close();
		return 0;
	}
	
	public static String getProjectName(){
		boolean valid = false;
		String readval = null;
		
		Scanner sc = new Scanner(System.in);
		while(!valid){
			 readval = sc.nextLine();
			if(tcpClient.checkProjectName(readval)){
				valid = true;
			}else{
				System.out.println("Sorry but the name " + readval + " is already taken.");
				System.out.println("Please try again.");
			}
		}
		////sc.close();
		return readval;
	}
	
	public static String getGeneralString(){
		String readval = null;
		
		Scanner sc = new Scanner(System.in);
		readval = sc.nextLine();
		////sc.close();
		return readval.replace("'", "''");
	}
	
	public static String getProjectDate(){
		boolean valid = false;
		String readVal = null;
		String readDay = null;
		String readMonth = null;
		String readYear = null;
		Scanner sc = new Scanner(System.in);
		while(!valid){
			System.out.println("Choose a day (ex: 09).");
			readDay = sc.nextLine();
			System.out.println("Choose a month (ex: 02).");
			readMonth = sc.nextLine();
			System.out.println("Choose a year (ex: 2030).");
			readYear = sc.nextLine();
			readVal = readDay + '/' + readMonth + '/' + readYear;
			if(checkValidDate(readVal)){
				valid = true;
			}else{
				System.out.println("Sorry but the date " + readVal + " is not valid.");
				System.out.println("Please try again.");
			}
		}
		////sc.close();
		readVal = readYear + '-' + readMonth + '-' + readDay;
		return readVal;
	}
	
	private static boolean checkValidDate(String dateInString){
		//The conversion segment of this function was taken from the website http://www.mkyong.com/java/how-to-convert-string-to-date-java/
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		formatter.setLenient(false);
		Date currentDate = new Date();
		try {
			
			if(Main.verbose)
				System.out.println("TEST@checkValidDate: Got the following date in String format: " + dateInString);
			
			Date date = formatter.parse(dateInString);
			
			if(Main.verbose)
				System.out.println("TEST@checkValidDate: Got the following date from String conversion: " + date);
			
			if(currentDate.compareTo(date) < 0){
				return true;
			}

		} catch (ParseException e) {
			return false;
		}	
		
		return false;
	}
	
	public static String getMoney(boolean allowZero){
		boolean valid = false;
		String readval = null;
		
		Scanner sc = new Scanner(System.in);
		while(!valid){
			readval = sc.nextLine();
			if (checkIfInt(readval) && Integer.parseInt(readval) == 0 && allowZero){
				valid = true;
			}else if(checkIfInt(readval) && Integer.parseInt(readval) > 0){
				valid = true;
			}else{
				System.out.println("Sorry but the ammount " + readval + " is not valid.");
				System.out.println("Please try again.");
			}
		}
		//sc.close();
		return readval;
	}
	
	public static int getHowManyVoteOptions(){
		boolean valid = false;
		int count = 0;
		String readval = null;
		
		Scanner sc = new Scanner(System.in);
		while(!valid){
			readval = sc.nextLine();
			if(checkIfInt(readval) && (Integer.parseInt(readval) > 1 || Integer.parseInt(readval) == 0)){
				count = Integer.parseInt(readval);
				valid = true;
			}else{
				System.out.println("Sorry but the ammount of vote options " + readval + " is not valid.");
				System.out.println("Please try again.");
			}
		}
		
		if(Main.verbose)
			System.out.println("Got the following ammount of vote options: " + count);
		//sc.close();
		return count;
	}
	
	private static boolean checkIfInt(String str){
		int radix = 10;
		Scanner sc = new Scanner(str.trim());
		
		if(!sc.hasNextInt(radix)) return false;
		sc.nextInt(radix);
		return !sc.hasNext();
	}
	
	public static void createTierMenu(int totalTiers, String project){
		boolean valid = false;
		String readval = null;
		int counter = 0;
		
		Scanner sc = new Scanner(System.in);
		
		if (totalTiers > 1){
			System.out.println("For starters how many tiers do you wish to create for now? You can add/remove tiers later on.");
			while(!valid){
				readval = sc.nextLine();
				if(checkIfInt(readval) && Integer.parseInt(readval) > 0){
					valid = true;
					totalTiers = Integer.parseInt(readval);
				}else{
					System.out.println("Sorry but number " + readval + " is either invalid or less than 1. ");
					System.out.println("Please try again.");
				}
			}
		}
		
		while(counter < totalTiers){
			valid = false;
			menuNavigation.createTier(project);
			counter++;
		}
		//sc.close();
	}
	
	public static boolean areYouSure(){
		System.out.println("Are you sure you want to do that?:");
		System.out.println("\t0. Yes");
		System.out.println("\t1. No");
		
		String[] ansArr = {"1","0"};
		
		int answer = getMenuAnswer(ansArr);
		
		if(answer == 0){
			return true;
		}else if(answer == 1){
			return false;
		}else{
			System.out.println("Err: AreYouSure Menu - Switch case not found for " + answer);
		}
		return false;
	}
}
