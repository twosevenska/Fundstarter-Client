package FundstarterClient;

import java.util.ArrayList;
import java.util.Hashtable;

import FundstarterClient.inputCheck;
import globalClasses.menu_list;

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
				logged = true;
			if(verbose)
				System.out.println("TEST@loginCheck: UserID = " + userId);
		}else{
			loginMenu();
		}
	}
	
	private static void registerCheck(){
		String user;
		String pass;
		boolean status = false;
		if(!logged){
			System.out.println("Username: ");
			user = inputCheck.getGeneralString();
			System.out.println("Password: ");
			pass = inputCheck.getGeneralString();
			status = tcpClient.registerUser(user, pass);
			if(status){
				System.out.println("Thanks for registering! You can now login.");
			}else{
				System.out.println("Sorry, but we couldn't register you. Please try another Username.");
			}
		}
		
	}
	
	private static void loginMenu(){
		String wallet = tcpClient.checkWallet(userId); //Replace this so that it gets the wallet value from the server
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
		boolean answer = false;
		System.out.println("How much do you wish to add? ");
		walletDOSH = inputCheck.getMoney();
		answer = tcpClient.addMoneyWallet(userId, walletDOSH);
		if (answer){
			System.out.println("Your wallet has been updated. Capitalism, ho!");
		}else{
			System.out.println("Sorry but we couldn't add the money right now.");
			System.out.println("Please try again later.");
		}
			
	}
	
	private static void createProjectMenu(){
		String projName;
		String descri;
		String endDate;
		String reqAmmount;
		boolean status = false;
		
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
		
		status = tcpClient.createProject(userId, projName, descri, endDate, reqAmmount);
		
		if(status){
			System.out.println("Awesome, you're almost finished, now you'll just need to go over your tiers and rewards for the project.");
			inputCheck.createTierMenu(2, projName);
		}else{
			System.out.println("Sorry but we couldn't create the Project right now.");
			System.out.println("Please try again later.");
		}
		
	}
	
	
	public static void createTier(String project){
		String descri;
		String reqAmmount;
		boolean status = false;
		
		System.out.println("What's the reward?");
		descri = inputCheck.getGeneralString();
		
		System.out.println("What's the minimum ammount required for this tier?");
		reqAmmount = inputCheck.getMoney();
		
		status = tcpClient.createTier(userId, descri, reqAmmount);
		
		if(status){
			System.out.println("Tier accepted :)");
		}else{
			System.out.println("Sorry but we couldn't create this tier right now.");
			System.out.println("Please try again later.");
		}
	}
	
	public static void showProjectsMenu(boolean owned){
		boolean oldFlag = false;
		String[] ansArr = {"1","2"};
		menu_list projListObject;
		String[] projListArray;
		String[] projListIds;
 		String[] projListAnswer;
		int projListSize;
		int answer;
		
		System.out.println("Do you wish to only see Active Projects?");
		System.out.println("Choose an option:");
		System.out.println("\t1. Yes");
		System.out.println("\t2. No");
		answer = inputCheck.getMenuAnswer(ansArr);
		
		switch (answer){
		case 1:	break;
		case 2: oldFlag = true;
			break;
		default:System.out.println("Err: Show Old Menu - Switch case not found for " + answer);
				break;
		}
		
		
		projListObject = tcpClient.getProjectsList(oldFlag);
		projListArray = projListObject.menuString;
		projListIds = projListObject.menuID;
		projListSize = projListArray.length;
		projListAnswer = createAnswerList(projListSize);
		
		for(String str : projListArray)
			System.out.println(str);
		
		answer = inputCheck.getMenuAnswer(projListAnswer);
		if(answer > 0 && answer < projListSize)
			showProjectOptionMenu(projListIds[answer]);
	}
	
	private static String[] createAnswerList(int size){
		String[] answerList = new String[size];
		
		for(int i = 0; i<size; i++){
			answerList[i] = Integer.toString(i);
		}
		
		return answerList;
	}
	
	public static void showProjectOptionMenu(String projID){
		boolean active = false;
		boolean admin = false;
		String title = null;
		String progress = null;
		String description = null;
		String endDate = null;
		int answer;
		Hashtable<String, String> projectInfo;
		
		projectInfo = tcpClient.getProjectData(userId, projID);
		
		if(projectInfo.get("active").compareTo("0") == 0)
			active = true;
		
		if(active){
			title = projectInfo.get("title");
			progress = projectInfo.get("progress");
			description = projectInfo.get("description");
			endDate = projectInfo.get("endDate");
			System.out.println("Project - " + title);
			System.out.println("Status: " + progress + "\t Closes on: " + endDate);
			System.out.println("Description: - " + description);
		}else{
			title = projectInfo.get("title");
			progress = projectInfo.get("progress");
			description = projectInfo.get("description");
			System.out.println("Project - " + title);
			System.out.println("Status: " + progress);
			System.out.println("Description: - " + description);
		}
		
		admin = tcpClient.checkAdmin(userId, projID);

		System.out.println("Choose an option:");
		System.out.println("\t0. Main menu");
		System.out.println("\t1. View Reward tiers");
		System.out.println("\t2. View Message Board");
		if(admin && active){
			System.out.println("\t3. Cancel Project");
			System.out.println("\t4. Create Tier Reward");
			String[] ansArr = {"1","2","3","4","0"};
			answer = inputCheck.getMenuAnswer(ansArr);
		}else{
			String[] ansArr = {"1","2","0"};
			answer = inputCheck.getMenuAnswer(ansArr);
		}
		
		switch (answer){
			case 0: break;
			case 1:	listRewardsMenu(projID);
					break;
			case 2:	listNotificationsMenu(projID);
					break;
			case 3:	boolean nuke = inputCheck.areYouSure();
					if(nuke){
						boolean nukeResult = tcpClient.nukeProject(userId, projID);
						if(nukeResult){
							System.out.println("Project deleted. We hope your next project is even more amazing :)");
						}else{
							System.out.println("Sorry but we couldn't delete this project right now.");
							System.out.println("Please try again later.");
						}
					}
					break;
			case 4: createTier(projID);
					break;
			default:System.out.println("Err: User Panel Menu - Switch case not found for " + answer);
					break;
		}
	}
	
	public static void listRewardsMenu(String projID){
		menu_list rewListObject;
		String[] rewList = null;
		String[] rewListId = null;
		String[] rewListAnswer = null;
		int rewListSize = 0;
		int answer;
		
		System.out.println("Choose an option:");
		
		rewListObject = tcpClient.getRewardsMenu(projID);
		rewList = rewListObject.menuString;
		rewListId = rewListObject.menuID;
		rewListSize = rewList.length;
		rewListAnswer = createAnswerList(rewListSize);
		
		for(String str : rewList)
			System.out.println(str);
		
		answer = inputCheck.getMenuAnswer(rewListAnswer);
		if(answer == 0){
			showProjectOptionMenu(projID);
		}else if(answer > 1 && answer <= rewListSize){
			showRewardMenu(projID, rewListId[answer]);
		}else{
			System.out.println("Err: Invalid option on reward selection menu.");
		}
	}
	
	private static void showRewardMenu(String projID, String rewID){
		boolean active = true;
		boolean admin = false;
		String title = null;
		String ammount = null;
		String description = null;
		String pledgeAmmount = "0";
		Hashtable<String, String> tierInfo;
		
		tierInfo = tcpClient.getTierInfo(userId, projID, rewID);
		ammount = tierInfo.get("ammount");
		title = "Pledge $".concat(ammount).concat("  or more");;
		description = tierInfo.get("description");
		pledgeAmmount = tierInfo.get("pledgeAmmount");
		
		
		if(tierInfo.get("active").compareTo("0") == 0)
			active = true;
		admin = tcpClient.checkAdmin(userId, projID);
		
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
			if(admin && active){
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
						if(nuke){
							boolean nukeResult = tcpClient.nukeTier(userId, projID, rewID);
							if(nukeResult){
								System.out.println("Tier deleted.");
							}else{
								System.out.println("Sorry but we couldn't remove this tier right now.");
								System.out.println("Please try again later.");
							}
						}
						break;
				default:System.out.println("Err: User Panel Menu - Switch case not found for " + answer);
						break;
			}
		}
	}
	
	private static void addPledgeMenu(String projID, String rewID){
		System.out.println("Please choose the ammount you wish add to this tier:");
		String pledgeDosh = inputCheck.getMoney();
		boolean result = tcpClient.addPledge(userId, rewID, pledgeDosh);
		if(result){
			System.out.println("Pledge successfull. Thank you!");
		}else{
			System.out.println("Ops, something went wrong :?");
			System.out.println("Please try again later.");
		}
	}
	
	private static void changePledgeMenu(String projID, String rewID, boolean modify){
		if(modify){
			System.out.println("Please choose the ammount you wish to change it to:");
			String pledgeDosh = inputCheck.getMoney();
			boolean result = tcpClient.changePledge(userId, rewID, pledgeDosh);
			if(result){
				System.out.println("Pledge changed.");
			}else{
				System.out.println("Ops, something went wrong :?");
				System.out.println("Please try again later.");
			}
		}else{
			boolean result = tcpClient.removePledge(userId, rewID);
			if(result){
				System.out.println("Pledge removed. Hope you find another project to pledge :)!");
			}else{
				System.out.println("Ops, something went wrong :?");
				System.out.println("Please try again later.");
			}
		}
	}
	
	public static void listVoteOptions(String projID){
		menu_list voteListObject;
		String[] voteList = null;
		String[] voteListId = null;
		String[] voteListAnswer = null;
		int voteListSize = 0;
		int answer;
		
		System.out.println("Please choose the option you wish to vote on:");
		
		voteListObject = tcpClient.getVoteOptions(projID);
		voteList = voteListObject.menuString;
		voteListId = voteListObject.menuID;
		voteListSize = voteList.length;
		voteListAnswer = createAnswerList(voteListSize);
		
		for(String str : voteList)
			System.out.println(str);
		
		answer = inputCheck.getMenuAnswer(voteListAnswer);
		if(answer == 0){
			showProjectOptionMenu(projID);
		}else if(answer > 1 && answer <= voteListSize){
			showRewardMenu(projID, voteListId[answer]);
		}else{
			System.out.println("Err: Invalid option on vote selection menu.");
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
