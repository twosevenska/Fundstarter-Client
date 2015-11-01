package fundstarterClient;

import java.util.Hashtable;

import fundstarterClient.inputCheck;
import globalClasses.Menu_list;

public class menuNavigation {
	
	static boolean logged = false;
	static int userId = 0;
	
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
		System.out.println("\t0. Exit");
		String[] ansArr = {"1","2","3","0"};
		int answer = inputCheck.getMenuAnswer(ansArr);
		
		switch (answer){
			case 1:	loginCheck();
					break;
			case 2:	registerCheck();
					break;
			case 3:	showProjectsMenu();
					break;
			case 0:	System.exit(1);
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
			if(Main.verbose)
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
		System.out.println("\t2. Show your rewards");
		System.out.println("\t3. Create a Project");
		System.out.println("\t4. My Projects");
		String[] ansArr = {"1","2","3","4","0"};
		int answer = inputCheck.getMenuAnswer(ansArr);
		
		switch (answer){
			case 0: break;
			case 1:	addWalletMoney();
					break;
			case 2:	showYourRewards();
					break;
			case 3:	createProjectMenu();
					break;
			case 4:	showMyProjectsMenu();
					break;
			default:System.out.println("Err: User Panel Menu - Switch case not found for " + answer);
					break;
		}
	}
	
	private static void addWalletMoney(){
		String walletDOSH = "0";
		boolean answer = false;
		System.out.println("How much do you wish to add? ");
		walletDOSH = inputCheck.getMoney(true);
		
		if(Integer.parseInt(walletDOSH) == 0){
			System.out.println("You chose to add 0 to your wallet. Returning you to the previous menu.");
			loginMenu();
		}
		
		answer = tcpClient.addMoneyWallet(userId, walletDOSH);
		if (answer){
			System.out.println("Your wallet has been updated. Capitalism, ho!");
		}else{
			System.out.println("Sorry but we couldn't add the money right now.");
			System.out.println("Please try again later.");
		}
			
	}
	
	private static void showYourRewards(){
		String[] ansArr = {"0"};
		
		String[] rewardsList = tcpClient.getMyRewards(userId).menuString;
		
		for(String str:rewardsList)
			System.out.println(str);
		
		System.out.println("\n\nChoose an option:");
		System.out.println("\t0. Return to previous menu");
		
		int answer = inputCheck.getMenuAnswer(ansArr);
		if(answer == 0)
			loginMenu();
	}
	
	private static void createProjectMenu(){
		String projName;
		String descri;
		String endDate;
		String reqAmmount;
		boolean status = false;
		String[] voteOptions;
		int voteOptionsCount;
		
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
		reqAmmount = inputCheck.getMoney(false);
		
		System.out.println("How many vote options do you want?");
		voteOptionsCount = inputCheck.getHowManyVoteOptions();
		
		voteOptions = addVoteOption(voteOptionsCount);
		
		status = tcpClient.createProject(userId, projName, descri, endDate, reqAmmount, voteOptions);
		
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
		reqAmmount = inputCheck.getMoney(false);
				
		String projId = tcpClient.getProjectId(project); 
		
		status = tcpClient.createTier(userId, descri, reqAmmount, projId);
		
		if(status){
			System.out.println("Tier accepted :)");
		}else{
			System.out.println("Sorry but we couldn't create this tier right now.");
			System.out.println("Please try again later.");
		}
	}
	
	private static String[] addVoteOption(int voteOptionsCount){
		String[] voteOptions = new String[voteOptionsCount];
		int i = 0;
		
		for(i = 0; i < voteOptionsCount; i++){
			System.out.println("What's the vote option?");
			voteOptions[i] = inputCheck.getGeneralString();
		}
		
		return voteOptions;
	}
	
	public static void showProjectsMenu(){
		boolean oldFlag = false;
		String[] ansArr = {"1","2"};
		Menu_list projListObject;
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
		
		System.out.println("Choose an option:");
		
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
	
	public static void showMyProjectsMenu(){
		Menu_list projListObject;
		String[] projListArray;
		String[] projListIds;
 		String[] projListAnswer;
		int projListSize;
		int answer;
		
		projListObject = tcpClient.getMyProjectsList(userId);
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
		
		if(Main.verbose)
			System.out.println("Test@showProjectOptionMenu: Got active status: " + active);
		
		if(active){
			title = projectInfo.get("title");
			progress = projectInfo.get("progress").concat("%");
			description = projectInfo.get("description");
			endDate = projectInfo.get("endDate");
			System.out.println("Project - " + title);
			System.out.println("Status: " + progress + "\t Closes on: " + endDate);
			System.out.println("Description: - " + description);
		}else{
			title = projectInfo.get("title");
			progress = projectInfo.get("progress");
			description = projectInfo.get("description");
			String str;
			
			if(Integer.parseInt(progress) < 100 ){
				str = "Funding Unsuccessful/Canceled with ";
			}else{
				str = "Funded with ";
			}
				
			System.out.println("Project - " + title);
			System.out.println("Status: " + str + progress);
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
			System.out.println("\t5. Check vote status");
			String[] ansArr = {"1","2","3","4","5","0"};
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
			case 4: createTier(title);
					break;
			case 5: showVotes(projID);
			break;
			default:System.out.println("Err: User Panel Menu - Switch case not found for " + answer);
					break;
		}
	}
	
	public static void listRewardsMenu(String projID){
		Menu_list rewListObject;
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
		}else if(answer > 0 && answer <= rewListSize){
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
		String[] ansArr2;
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
		
		if(!active || !logged){
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
				ansArr2 = new String[]{"1","2","3","4","0"};
			}else{
				ansArr2 = new String[]{"1","2","3","0"};
			}
			
			int answer = inputCheck.getMenuAnswer(ansArr2);
			
			switch (answer){
				case 0: listRewardsMenu(projID);
						break;
				case 1:	addPledgeMenu(projID, rewID, Integer.parseInt(ammount));
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
	
	private static void addPledgeMenu(String projID, String rewID, int minimum){
		String pledgeDosh = null;
		boolean invalid = true;
		String voteId = listVoteOptions(projID);
		int currentWalletMoney = Integer.parseInt(tcpClient.checkWallet(userId));
		System.out.println("Please choose the ammount you wish to add to this tier:");
		
		while(invalid){
			pledgeDosh = inputCheck.getMoney(true);
			int moneyResult = currentWalletMoney - Integer.parseInt(pledgeDosh);
			if(Integer.parseInt(pledgeDosh) >= minimum && moneyResult >= 0){
				invalid = false;
			}else if(Integer.parseInt(pledgeDosh) == 0){
				System.out.println("You chose 0, so we're taking you back to the previous menu.");
				invalid = false;
				showRewardMenu(projID, rewID);
			}else if(moneyResult < 0){
				System.out.println("You don't have enough money in your wallet.");
				
				if(Main.verbose)
					System.out.println("Gibe money please\nHUEHUEHUEHUEHUEHUE");
			}else{
				System.out.println("Sorry, but the ammount inserted is lower than the minimum ammount required for this tier.");
			}
		}
		
		boolean result = tcpClient.addPledge(userId, projID, rewID, pledgeDosh, voteId);
		if(result){
			System.out.println("Pledge successfull. Thank you!");
		}else{
			System.out.println("Ops, something went wrong :?");
			System.out.println("Please try again later.");
		}
	}
	
	private static void changePledgeMenu(String projID, String rewID, boolean modify){
		//This code is for a possible future instruction. Missing the SQL Query
		//section, wasn't completed since it wasn't needed for this goal. Also missing
		//minimum amount check.
		System.out.println("We're sorry but this feature is still being cooked in the magic cauldron.");
		/*if(modify){
			System.out.println("Please choose the amount you wish to change it to:");
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
		}*/
	}
	
	public static String listVoteOptions(String projID){
		Menu_list voteListObject;
		String[] voteList = null;
		String[] voteListId = null;
		String[] voteListAnswer = null;
		int voteListSize = 0;
		int answer;
		
		voteListObject = tcpClient.getVoteOptions(projID);
		voteList = voteListObject.menuString;
		voteListId = voteListObject.menuID;
		voteListSize = voteList.length;
		
		if(voteListSize != 1){
			System.out.println("Please choose the option you wish to vote on:");
			voteListAnswer = createAnswerList(voteListSize);
			
			for(String str : voteList)
				System.out.println(str);
			
			answer = inputCheck.getMenuAnswer(voteListAnswer);
			if(answer == 0){
				showProjectOptionMenu(projID);
			}else if(answer > 0 && answer <= voteListSize){
				return voteListId[answer];
			}else{
				System.out.println("Err: Invalid option on vote selection menu.");
			}
		}
		return "0";
	}
	
	public static void showVotes(String projId){
		int answer;
		String[] voteList = null;
		
		voteList = tcpClient.getVoteResults(projId);
		
		for(String str: voteList)
			System.out.println(str);
		
		System.out.println("Choose an option:");
		System.out.println("\t0. Previous menu");
		String[] ansArr = {"0"};
		answer = inputCheck.getMenuAnswer(ansArr);
		
		switch (answer){
		case 0:	showProjectOptionMenu(projId);
			break;
		default:System.out.println("Err: Show Votes - Switch case not found for " + answer);
				break;
		}
	}
	
	public static void listNotificationsMenu(String projID){
		Menu_list notListObject;
		String[] notList = null;
		String[] notListId = null;
		String[] notListAnswer = null;
		int notListSize = 0;
		int answer;
		
		System.out.println("Choose an option:");
		
		notListObject = tcpClient.getMessageBoard(projID);
		notList = notListObject.menuString;
		notListId = notListObject.menuID;
		notListSize = notList.length;
		notListAnswer = createAnswerList(notListSize);
		
		for(String str : notList)
			System.out.println(str);
		
		answer = inputCheck.getMenuAnswer(notListAnswer);
		if(answer == 0){
			showProjectOptionMenu(projID);
		}else if(answer == 1 && logged){
			createNotificationMenu(projID);
		}else if(answer == 1 && !logged){
			System.out.println("Sorry but you need to login in order to post a message.");
		}else if(answer > 1 && answer <= notListSize){
			showNotificationMenu(projID, notListId[answer]);
		}else{
			System.out.println("Err: Invalid option on Message menu.");
		}
	}
	
	private static void showNotificationMenu(String projID, String notID){
		boolean admin = false;
		boolean answered = false;
		String title = null;
		String user = null;
		String description = null;
		String notAnswer = null;
		Hashtable<String, String> answerHash = null;
		String[] ansArr = null;
		
		
		answerHash = tcpClient.getNotification(userId, notID);
		title = answerHash.get("title");
		user = answerHash.get("user");
		description = answerHash.get("description");
		notAnswer = answerHash.get("notAnswer");
		
		if(answerHash.get("answered").compareTo("0") == 0){
			answered = true;
		}
		
		admin = tcpClient.checkAdmin(userId, projID);
		
		System.out.println("Title: " + title);
		System.out.println("by " + user);
		System.out.println("Message: " + description);
		
		if(answered)
			System.out.println("Answer: " + notAnswer);
		
		System.out.println("Choose an option:");
		System.out.println("\t1. Return to previous menu");
		if(!answered && admin){
			System.out.println("\t2. Answer");
			ansArr = new String[2];
			ansArr[0] = "1";
			ansArr[1] = "2";
		}else{
			ansArr = new String[1];
			ansArr[0] = "1";
		}
		
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
		String title = null;
		String descri = null;
		boolean result = false;
		
		System.out.println("What's the title?");
		title = inputCheck.getGeneralString();
		System.out.println("Please give a small description.");
		descri = inputCheck.getGeneralString();
		
		result = tcpClient.createNotification(userId, projID, title, descri);
		
		if(result){
			System.out.println("Message thread created succesfully. We hope we can answer it soon.");
		}else{
			System.out.println("Sorry but we couldn't create this message thread.");
			System.out.println("Please try again later.");
		}
	}
	
	private static void answerNotificationMenu(String projID, String notID){
		String descri;
		boolean result = false;
		
		System.out.println("Please type your answer.");
		descri = inputCheck.getGeneralString();
		
		result = tcpClient.answerNotification(userId, projID, notID, descri);
		
		if(result){
			System.out.println("Message answered succesfully.");
		}else{
			System.out.println("Sorry but we couldn't create this reply.");
			System.out.println("Please try again later.");
		}
	}
}
