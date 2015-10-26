package FundstarterClient;

import java.util.ArrayList;

import FundstarterClient.inputCheck;

public class menuNavigation {
	
	static boolean logged = false;
	static int userId = 0;
	static boolean verbose = true;
	
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
			case 1:	loginCheck();
					break;
			case 2:	registerCheck();
					break;
			case 3:	showProjectsMenu(false);
					break;
			default:System.out.println("Err: Main Menu - Switch case not found for " + answer);
					break;
		}
		
	}
	
	private static void loginCheck(){
		String user;
		String pass;
		if(!logged){
			System.out.println("Username: ");
			user = inputCheck.getGeneralString();
			System.out.println("Password: ");
			pass = inputCheck.getGeneralString();
			userId = tcpClient.loginUser(user, pass);
			if(userId != 0)
				logged = false;
			if(verbose)
				System.out.println("TEST@loginCheck: UserID = " + userId);
		}else{
			loginMenu();
		}
	}
	
	private static void registerCheck(){
		String user;
		String pass;
		if(!logged){
			System.out.println("Username: ");
			user = inputCheck.getGeneralString();
			System.out.println("Password: ");
			pass = inputCheck.getGeneralString();
			// Call function to check if user can be registered
		}
		
	}
	
	private static void loginMenu(){
		int wallet = 100; //Replace this so that it gets the wallet value from the server
		System.out.println("Wallet: " + wallet);
		System.out.println("Choose an option:");
		System.out.println("\t0. Main menu");
		System.out.println("\t1. Add money to wallet");
		System.out.println("\t2. Show Projects you pledged");
		System.out.println("\t3. Create a Project");
		String[] ansArr = {"1","2","3","0"};
		int answer = inputCheck.getMenuAnswer(ansArr);
		
		switch (answer){
			case 0: break;
			case 1:	addWalletMoney();
					break;
			case 2:	showProjectsMenu(true);
					break;
			case 3:	createProjectMenu();
					break;
			default:System.out.println("Err: User Panel Menu - Switch case not found for " + answer);
					break;
		}
	}
	
	private static void addWalletMoney(){
		String walletDOSH = "0";
		System.out.println("How much do you wish to add? ");
		walletDOSH = inputCheck.getMoney();
		//Call function to add money on server
	}
	
	private static void createProjectMenu(){
		String projName;
		String descri;
		String endDate;
		String reqAmmount;
		
		System.out.println("Hi! We're very happy that you chose Fundstarter to host your crowdsourcing idea!");
		System.out.println("But let's not get ahead of ourselves, for starters what's the name of your project?");
		System.out.println("Tip: Try to choose something catchy without making it alien to your idea :)");
		projName = inputCheck.getProjectName();
		
		System.out.println("Now tell us a bit about your project. This will be your Project description.");
		descri = inputCheck.getGeneralString();
		
		System.out.println("When do you want it to end?");
		endDate = inputCheck.getProjectDate();
		
		System.out.println("We hope that ends up being a day to remember :D");
		System.out.println("We're still missing how much money you need:");
		reqAmmount = inputCheck.getMoney();
		
		//Call function to create project entry in database without the tiers
		
		System.out.println("Awesome, you're almost finished, now you'll just need to go over your tiers and rewards for the project.");
		inputCheck.createTierMenu(2, projName);
		
	}
	
	
	public static void createTier(String project){
		String descri;
		String reqAmmount;
		
		System.out.println("What's the reward?");
		descri = inputCheck.getGeneralString();
		
		System.out.println("What's the minimum ammount required for this tier?");
		reqAmmount = inputCheck.getMoney();
		
		//Call function to add tier entry to the project in the database
	}
	
	public static void showProjectsMenu(boolean owned){
		//Adapt this function to get part of the menu from the server
		boolean oldFlag = false;
		String[] ansArr = {"1","2"};
		String[] ansArr2 = new String[1000];
		ArrayList projList = new ArrayList();
		int answer;
		int i = 1;
		
		System.out.println("Do you wish to only see Active Projects?");
		System.out.println("Choose an option:");
		System.out.println("\t1. Yes");
		System.out.println("\t2. No");
		answer = inputCheck.getMenuAnswer(ansArr);
		
		switch (answer){
		case 1:	oldFlag = true;
				break;
		case 2: break;
		default:System.out.println("Err: Show Old Menu - Switch case not found for " + answer);
				break;
		}
		
		//Call function to get an object with a project names and ids
		/*System.out.println("Choose an option:");
		ansArr2[0] = "0";
		System.out.println("\t0. Main menu");
		for(Object t: projList){
			if()
			System.out.println("\t"+i+". Project - "+t[i][1]);
			ansArr2[i] = ""+i;
			i++;
		}
		answer = inputCheck.getMenuAnswer(ansArr2);
		if(answer > 0 && answer <= i)
			showProjectOptionMenu(projList[answer][0]);*/
	}
	
	public static void showProjectOptionMenu(String projID){
		boolean active = true;
		boolean admin = false;
		String title = null;
		String progress = null;
		String description = null;
		String endDate = null;
		
		//Call function to get all info from project
		//Call function to check if active
		
		if(active){
			/*
			 * title = ;
			 * progress = ;
			 * description = ;
			 * endDate = ;
			 */
			System.out.println("Project - " + title);
			System.out.println("Status: " + progress + "\tCloses on: " + endDate);
			System.out.println("Description: - " + description);
		}else{
			/*
			 * title = ;
			 * progress = ;
			 * description = ;
			 */
			System.out.println("Project - " + title);
			System.out.println("Status: " + progress);
			System.out.println("Description: - " + description);
		}
		
		//Call function to check if admin

		System.out.println("Choose an option:");
		System.out.println("\t0. Main menu");
		System.out.println("\t1. View Reward tiers");
		System.out.println("\t2. View Message Board");
		if(admin && !active){
			System.out.println("\t3. Cancel Project");
			String[] ansArr = {"1","2","3","0"};
		}
		String[] ansArr = {"1","2","0"};
		
		int answer = inputCheck.getMenuAnswer(ansArr);
		
		switch (answer){
			case 0: break;
			case 1:	listRewardsMenu(projID);
					break;
			case 2:	listNotificationsMenu(projID);
					break;
			case 3:	boolean nuke = inputCheck.areYouSure();
					//if(nuke)
						//Call function to delete project
					break;
			default:System.out.println("Err: User Panel Menu - Switch case not found for " + answer);
					break;
		}
	}
	
	public static void listRewardsMenu(String projID){
		//Adapt this for server side list
		boolean admin = false;
		String[] ansArr2 = new String[1000];
		String[][] rewList = null;
		int answer;
		int i;
		
		//Call function to get an object with the rewards title and id
		
		System.out.println("Choose an option:");
		ansArr2[0] = "0";
		System.out.println("\t0. Previous menu");
		System.out.println("\t1. Create reward");
		for(i = 2; i<rewList.length;i++){
			System.out.println("\t"+i+". Project - "+rewList[i][1]);
			ansArr2[i] = ""+i;
		}
		answer = inputCheck.getMenuAnswer(ansArr2);
		if(answer == 0){
			showProjectOptionMenu(projID);
		}else if(answer == 1){
			if(admin){
				createTier(projID);
			}else{
				System.out.println("I can't let you do that.");
			}
		}else if(answer > 1 && answer <= i){
			showRewardMenu(projID, rewList[i][0]);
		}else{
			System.out.println("Err: Invalid option on reward selection menu.");
		}
	}
	
	private static void showRewardMenu(String projID, String rewID){
		/*TODO: 
		 * Make menu
		 * Create a pledge
		 * Modify a pledge
		 * Remove pledge
		 * Remove tier
		 */
		boolean active = true;
		boolean admin = false;
		String title = null;
		String ammount = null;
		String description = null;
		String pledgeAmmount = "0";
		
		//Call function to get all info from project
		
		System.out.println("Title - " + title);
		System.out.println("Minimum ammount: " + ammount + "\tPledge: " + pledgeAmmount);
		System.out.println("Description:");
		System.out.println(description);
		
		if(!active){
			System.out.println("Choose an option:");
			System.out.println("\t0. Previous menu");
			String[] ansArr = {"0"};
			int answer = inputCheck.getMenuAnswer(ansArr);
			if(answer == 0)
				listRewardsMenu(projID);
		}else{
			System.out.println("Choose an option:");
			System.out.println("\t0. Previous menu");
			System.out.println("\t1. Add a pledge");
			System.out.println("\t2. Change pledge");
			System.out.println("\t3. Remove pledge");
			if(admin && !active){
				System.out.println("\t4. Remove tier");
				String[] ansArr = {"1","2","3","4","0"};
			}
			String[] ansArr = {"1","2","3","0"};
			
			int answer = inputCheck.getMenuAnswer(ansArr);
			
			switch (answer){
				case 0: listRewardsMenu(projID);
						break;
				case 1:	addPledgeMenu(projID, rewID);
						break;
				case 2:	changePledgeMenu(projID, rewID, true);
						break;
				case 3:	changePledgeMenu(projID, rewID, false);
						break;
				case 4:	boolean nuke = inputCheck.areYouSure();
						//if(nuke)
							//Call function to delete tier
						break;
				default:System.out.println("Err: User Panel Menu - Switch case not found for " + answer);
						break;
			}
		}
	}
	
	private static void addPledgeMenu(String projID, String rewID){
		System.out.println("Please choose the ammount you wish add to this tier:");
		String pledgeDosh = inputCheck.getMoney();
		//Call function to add pledge
	}
	
	private static void changePledgeMenu(String projID, String rewID, boolean modify){
		if(modify){
			System.out.println("Please choose the ammount you wish to change it to:");
			String pledgeDosh = inputCheck.getMoney();
			//Call function to change pledge value
		}else{
			//Call function to change value to 0 and check if there's no other pledge from this user -None-> change so it doesn't show up on owned 
		}
	}
	
	public static void listNotificationsMenu(String projID){
		//Adapt this for server side list
		boolean admin = false;
		String[][] notList = null;
		String[] ansArr2 = new String[1000];
		int i;
		int answer;
		//Call function to get a notifications object with all their titles
		
		System.out.println("Choose an option:");
		ansArr2[0] = "0";
		ansArr2[0] = "1";
		ansArr2[0] = "2";
		System.out.println("\t0. Main menu");
		System.out.println("\t1. Previous menu");
		System.out.println("\t2. Create ");// logged?
		for(i = 3; i<notList.length;i++){
			System.out.println("\t"+i+". Title - "+notList[i][1]);
			ansArr2[i] = ""+i;
		}
		answer = inputCheck.getMenuAnswer(ansArr2);
		if(answer == 1){
			showProjectOptionMenu(projID);
		}else if(answer == 2){
			createNotificationMenu(projID);
		}else if(answer > 3 && answer <= i){
			showNotificationMenu(projID, notList[answer][0]);
		}
	}
	
	private static void showNotificationMenu(String projID, String notID){
		boolean admin = false;
		boolean answered = false;
		String description = null;
		String notAnswer = null;
		
		//function to get a notification title, description and user
		//function to verify if it is answered
		//function to verify if admin
		
		System.out.println("Choose an option:");
		System.out.println("\t1. Return to previous menu");
		if(!answered && admin){
			System.out.println("\t2. Answer");
			String[] ansArr = {"1","2"};
		}
		String[] ansArr = {"1"};
		int answer = inputCheck.getMenuAnswer(ansArr);
		
		switch (answer){
			case 1:	listNotificationsMenu(projID);
					break;
			case 2:	answerNotificationMenu(projID, notID);
					break;
			default:System.out.println("Err: Main Menu - Switch case not found for " + answer);
					break;
		}
	}
	
	private static void createNotificationMenu(String projID){
		String title;
		String descri;
		
		System.out.println("What's the title?");
		title = inputCheck.getGeneralString();
		System.out.println("Please give a small description.");
		descri = inputCheck.getGeneralString();
		
		//Call function that sends this over to the server
	}
	
	private static void answerNotificationMenu(String projID, String notID){
		String descri;
		
		System.out.println("Please type your answer.");
		descri = inputCheck.getGeneralString();
		
		//Call function that sends this over to the server
	}
}
