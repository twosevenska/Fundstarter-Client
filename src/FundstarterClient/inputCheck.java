package FundstarterClient;

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
			if(true){//Change this to something that checks if the chosen name is available
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
		return readval;
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
		Date currentDate = new Date();
		try {
			
			Date date = formatter.parse(dateInString);
			
			if(currentDate.compareTo(date) < 0){
				return true;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}	
		
		return false;
	}
	
	public static String getMoney(){
		boolean valid = false;
		String readval = null;
		
		Scanner sc = new Scanner(System.in);
		while(!valid){
			readval = sc.nextLine();
			if(checkPositiveInt(readval)){
				valid = true;
			}else{
				System.out.println("Sorry but the ammount " + readval + " is not valid.");
				System.out.println("Please try again.");
			}
		}
		//sc.close();
		return readval;
	}
	
	private static boolean checkPositiveInt(String str){
		int temp;
		try { 
	        temp = Integer.parseInt(str);
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
		
		if(temp > 0)
			return true;
		return false;
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
				if(checkPositiveInt(readval)){
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
