package fundstarterClient;

import java.net.*;
import java.util.Hashtable;
import java.io.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import globalClasses.Com_object;
import globalClasses.Menu_list;
import globalClasses.Com_object.operationtype;

public class tcpClient {

	static Socket objsock = null;
	static ObjectOutputStream oos;
	static ObjectInputStream ois;
	static String currentServer = Main.serverAlphaAddress;
	
	public static boolean createTcpSocket() {
		int tryCounter = 0;
		int tryTotal = 8;
		int serversocket = 6000;
		int connectTimeout = 1000;
		
		while(tryCounter < tryTotal){
			try {
				// First open the socket
				if(Main.verbose)
					System.out.println("Connecting to address: " + currentServer);
				//Warning: If something goes wrong, check HERE!!!!
				Socket objsock = new Socket();
				objsock.connect(new InetSocketAddress(currentServer, serversocket), connectTimeout);
				if(Main.verbose)
					System.out.println("SOCKET=" + objsock);
				
				// Now open the object streams
				oos = new ObjectOutputStream(objsock.getOutputStream());
				ois = new ObjectInputStream(objsock.getInputStream());
				
				return true;
			} catch (UnknownHostException e){
				if(Main.verbose){
					System.out.println("Sock:" + e.getMessage());
					e.printStackTrace();
				}
			} catch (IOException e) {
				if(Main.verbose){
					System.out.println("IO:" + e.getMessage());
					//e.printStackTrace();
					System.out.println("Changing address");
				}
				if(tryCounter % 2 == 1){
					currentServer = Main.serverBetaAddress;
					Main.serverBetaAddress = Main.serverAlphaAddress;
					Main.serverAlphaAddress = currentServer;
				}
				tryCounter++;
			}
		}
		return false;
	}
	
	public static void closeTcpSocket(){
		if (objsock != null)
			try {
				objsock.close();
			} catch (IOException e) {
			    System.out.println("close:" + e.getMessage());
			    e.printStackTrace();
			}
	}
	
	private static Com_object sendThroughSocket(int userId, operationtype operation, Hashtable<String, String> content){
		boolean toSend = true;
		int nullCounter = 0;
		Com_object comIn = null;
		// Let's create the object
		Com_object comOut = new Com_object(userId, operation, content);
		comOut.generateIdPackage(userId);
		// And now send it
		while(toSend){
			try{
				oos.writeObject(comOut);
				// Better clean the socket
				oos.flush();
				toSend = false;
				// And now read
				comIn = (Com_object) ois.readObject();
				if(comIn == null){
					if(nullCounter == 3)
						exitProtocol();
					toSend = true;
					nullCounter++;
				}
			} catch (IOException e) {
				if(Main.verbose){
					System.out.println("IO:" + e.getMessage());
					//e.printStackTrace();
					System.out.println("Trying to reconnect");
				}
				if(!createTcpSocket())
					exitProtocol();
			} catch (ClassNotFoundException e) {
				System.out.println("Wrong Object:" + e.getMessage());
			}
		}
		return comIn;
	}
	
	private static Com_object sendThroughSocket(int userId, operationtype operation, Hashtable<String, String> content, Menu_list mList){
		boolean toSend = true;
		int nullCounter = 0;
		Com_object comIn = null;
		// Let's create the object
		Com_object comOut = new Com_object(userId, operation, content, mList);
		comOut.generateIdPackage(userId);
		// And now send it
		while(toSend){
			try{
				oos.writeObject(comOut);
				// Better clean the socket
				oos.flush();
				toSend = false;
				// And now read
				comIn = (Com_object) ois.readObject();
				if(comIn == null){
					if(nullCounter == 3)
						exitProtocol();
					toSend = true;
					nullCounter++;
				}
			} catch (IOException e) {
				if(Main.verbose){
					System.out.println("IO:" + e.getMessage());
					//e.printStackTrace();
					System.out.println("Trying to reconnect");
				}
				if(!createTcpSocket())
					exitProtocol();
			} catch (ClassNotFoundException e) {
				System.out.println("Wrong Object:" + e.getMessage());
			}
		}
		return comIn;
	}
	
	private static void exitProtocol(){
		System.out.println("We're sorry but we couldn't connect to the server.");
		System.out.println("Closing the client now.");
		if(Main.verbose){
			System.out.println("Babe, it's not you... It's me.");
		}
		System.out.println("Please try again later.");
		System.exit(0);
	}
	
	public static int loginUser(String username, String password){
		int userId = 0;
		Hashtable<String, String> loginData = new Hashtable<String, String>();
		
		loginData.put("username", username);
		loginData.put("password", password);
		
		if(Main.verbose){
			System.out.println("TEST@loginUser: Sending hash = " );
			System.out.println(loginData);
			System.out.println("TEST@loginUser: Sending everything now.");
		}
		Com_object comIn = sendThroughSocket(userId, operationtype.login, loginData);
		
		if(Main.verbose)
			System.out.println("TEST@loginUser: Now getting userID");
		
		userId = Integer.parseInt(comIn.elements.get("userId"));
		
		if(Main.verbose)
			System.out.println("TEST@loginUser: Got userID = "+ userId);
		
		return userId;
	}
	
	public static boolean registerUser(String username, String password){
		int userId = 0;
		int iStatus;
		Hashtable<String, String> loginData = new Hashtable<String, String>();
		
		loginData.put("username", username);
		loginData.put("password", password);
		
		if(Main.verbose){
			System.out.println("TEST@registerUser: Sending hash = " );
			System.out.println(loginData);
			System.out.println("TEST@registerUser: Sending everything now.");
		}
		Com_object comIn = sendThroughSocket(userId, operationtype.register, loginData);
		
		if(Main.verbose){
			System.out.println("TEST@registerUser: Now getting success status");
			System.out.println("TEST@registerUser: Got the hastable: " + comIn.elements);
		}
		
		String status = comIn.elements.get("status");
		iStatus = Integer.parseInt(status);
		
		if(Main.verbose)
			System.out.println("TEST@registerUser: Got iStatus = "+ iStatus);
		
		if(iStatus == 0)
			return true;
		return false;
	}
	
	
	public static String checkWallet(int userId){
		String walletAmmount = null;
		Hashtable<String, String> identification = new Hashtable<String, String>();
		
		identification.put("userId", Integer.toString(userId));
		
		if(Main.verbose)
			System.out.println("TEST@getWallet: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.check_wallet, identification);
		
		if(Main.verbose)
			System.out.println("TEST@getWallet: Now getting wallet ammount");
		
		walletAmmount = comIn.elements.get("wallet");
		
		if(Main.verbose)
			System.out.println("TEST@getWallet: Got walletAmmount = "+ walletAmmount);
		
		return walletAmmount;
	}
	
	public static boolean addMoneyWallet(int userId, String money){
		int mStatus;
		Hashtable<String, String> moneyHash = new Hashtable<String, String>();
		
		moneyHash.put("userId", Integer.toString(userId));
		moneyHash.put("wallet", money);
		
		if(Main.verbose)
			System.out.println("TEST@addMoneyWallet: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.add_wallet, moneyHash);
		
		if(Main.verbose)
			System.out.println("TEST@addMoneyWallet: Now getting wallet update confirmation");
		
		mStatus = Integer.parseInt(comIn.elements.get("money"));
		
		if(Main.verbose)
			System.out.println("TEST@addMoneyWallet: Got mStatus = "+ mStatus);
		
		if(mStatus == 0)
			return true;
		return false;
	}
	
	public static boolean createProject(int userId, String projName, String description,
										String endDate, String reqAmmount, String[] voteOptions){
		int status = 0;
		Hashtable<String, String> projectHash = new Hashtable<String, String>();
		Menu_list request = new Menu_list(voteOptions, null);
		
		projectHash.put("userId", Integer.toString(userId));
		projectHash.put("projName", projName);
		projectHash.put("description", description);
		projectHash.put("endDate", endDate);
		projectHash.put("reqAmmount", reqAmmount);
		
		if(Main.verbose)
			System.out.println("TEST@createProject: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.create_proj, projectHash, request);
		
		if(Main.verbose)
			System.out.println("TEST@createProject: Now getting project creation confirmation");
		
		status = Integer.parseInt(comIn.elements.get("projectStatus"));
		
		if(Main.verbose)
			System.out.println("TEST@createProject: Got status = "+ status);
		
		if(status == 0)
			return true;
		return false;
	}
	
	public static boolean createTier(int userId, String description, String reqAmmount, String projId){
		int status = 0;
		Hashtable<String, String> tierHash = new Hashtable<String, String>();
		
		tierHash.put("userId", Integer.toString(userId));
		tierHash.put("description", description);
		tierHash.put("reqAmmount", reqAmmount);
		tierHash.put("projId", projId);
		
		if(Main.verbose)
			System.out.println("TEST@createTier: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.add_meta, tierHash);
		
		if(Main.verbose)
			System.out.println("TEST@createTier: Now getting tier creation confirmation");
		
		status = Integer.parseInt(comIn.elements.get("tierStatus"));
		
		if(Main.verbose)
			System.out.println("TEST@createTier: Got status = "+ status);
		
		if(status == 0)
			return true;
		return false;
	}
	
	
	public static Menu_list getProjectsList(boolean oldFlag){
		Menu_list answer = null;
		int startIndex = 1;
		String[] strListRaw = null;
		String[] idListRaw = null;
		
		Hashtable<String, String> menuHash = new Hashtable<String, String>();
		
		if(Main.verbose)
			System.out.println("TEST@getProjectsList: Sending everything now.");
		
		if(oldFlag){
			Com_object comIn1 = sendThroughSocket(0, operationtype.see_all_proj_off, menuHash);
			Com_object comIn2 = sendThroughSocket(0, operationtype.see_all_proj_on, menuHash);
			
			strListRaw = concatStringArray(comIn1.menuList.menuString, comIn2.menuList.menuString);
			idListRaw = concatStringArray(comIn1.menuList.menuID, comIn2.menuList.menuID);
		}else{
			Com_object comIn = sendThroughSocket(0, operationtype.see_all_proj_on, menuHash);
			strListRaw = comIn.menuList.menuString;
			idListRaw = comIn.menuList.menuID;
		}
		
		if(Main.verbose)
			System.out.println("TEST@getProjectsList: Got the menu options.");
		
		String[] strList = formatStringArray(strListRaw, startIndex);
		strList[0] = "\t0. Main menu";
		String[] idList = formatIdArray(idListRaw, startIndex);
		idList[0] = "Wubba lubba dub dub";
		
		answer = new Menu_list(strList, idList);
		
		return answer;
	}
	
	public static Menu_list getMyProjectsList(int userId){
		Menu_list answer = null;
		int startIndex = 1;
		String[] strListRaw = null;
		String[] idListRaw = null;
		
		Hashtable<String, String> menuHash = new Hashtable<String, String>();
		
		menuHash.put("userId", Integer.toString(userId));
		
		if(Main.verbose)
			System.out.println("TEST@getMyProjectsList: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(0, operationtype.see_my_projs, menuHash);
		strListRaw = comIn.menuList.menuString;
		idListRaw = comIn.menuList.menuID;
		
		if(Main.verbose)
			System.out.println("TEST@getMyProjectsList: Got the menu options.");
		
		String[] strList = formatStringArray(strListRaw, startIndex);
		strList[0] = "\t0. Main menu";
		String[] idList = formatIdArray(idListRaw, startIndex);
		idList[0] = "Shut the **** up about moonmen";
		
		answer = new Menu_list(strList, idList);
		
		return answer;
	}
	
	private static String[] formatStringArray(String[] array1, int start){
		String[] result = new String[array1.length+start];
		int i = start;
		
		for(String str: array1){
			result[i] = "\t".concat(Integer.toString(i).concat(". ").concat(str));
			i++;
		}
		
		return result;
	}
	
	private static String[] formatIdArray(String[] array1, int start){
		String[] result = new String[array1.length+start];
		int i = start;
		
		for(String str: array1){
			result[i] = str;
			i++;
		}
		
		return result;
	}
	
	private static String[] concatStringArray(String[] array1, String[] array2){
		String[] result = new String[array1.length + array2.length];
		int i = 0;
		
		for(String str: array1){
			result[i] = str;
			i++;
		}
		
		for(String str: array2){
			result[i] = str;
			i++;
		}
		
		return result;
	}
	
	public static Hashtable< String, String> getProjectData(int userId, String projID){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		Hashtable<String, String> answerHash;
		
		requestHash.put("projId", projID);
		
		if(Main.verbose)
			System.out.println("TEST@getProjectData: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.see_proj, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@getProjectData: Now getting project info");
		
		answerHash = comIn.elements;
		
		if(Main.verbose)
			System.out.println("TEST@getProjectData: Got answerHash = "+ answerHash);
		
		return answerHash;
	}
	
	public static boolean checkAdmin(int userId, String projId){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("projId", projId);
		
		if(Main.verbose)
			System.out.println("TEST@checkAdmin: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.check_admin, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@checkAdmin: Now getting admin status confirmation");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("admin"));
		
		if(Main.verbose)
			System.out.println("TEST@checkAdmin: Got Admin status iAnswer = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static boolean nukeProject(int userId, String projID){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("projID", projID);
		
		if(Main.verbose)
			System.out.println("TEST@nukeProject: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.cancel_project, requestHash);
		
		if(Main.verbose){
			System.out.println("TEST@nukeProject: Now getting deletion confirmation");
			System.out.println("TEST@nukeProject: Got the hash: "+comIn.elements);
		}
		
		int iAnswer = Integer.parseInt(comIn.elements.get("delete"));
		
		if(Main.verbose)
			System.out.println("TEST@nukeProject: Got deletion status iAnswer = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static Menu_list getRewardsMenu(String projID){
		Menu_list answer = null;
		int startIndex = 1;
		String[] strListRaw = null;
		String[] idListRaw = null;
		
		Hashtable<String, String> menuHash = new Hashtable<String, String>();
		
		menuHash.put("projId", projID);
		
		if(Main.verbose)
			System.out.println("TEST@getRewardsMenu: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(0, operationtype.see_all_rewards, menuHash);
		strListRaw = comIn.menuList.menuString;
		idListRaw = comIn.menuList.menuID;
		
		if(Main.verbose){
			System.out.println("TEST@getRewardsMenu: Got the menu options: ");
			for(String str: strListRaw){
				System.out.println(str);
			}
		}
		
		String[] strList = formatStringArray(strListRaw, startIndex);
		strList[0] = "\t0. Previous menu";
		String[] idList = formatIdArray(idListRaw, startIndex);
		idList[0] = "WHATUP MY GLIP GLOPS";
		
		answer = new Menu_list(strList, idList);
		
		return answer;
	}
	
	public static Hashtable< String, String> getTierInfo(int userId, String projID, String rewID){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		Hashtable<String, String> answerHash;
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("projId", projID);
		requestHash.put("rewId", rewID);
		
		if(Main.verbose)
			System.out.println("TEST@getTierInfo: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.see_reward, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@getTierInfo: Now getting tier info");
		
		answerHash = comIn.elements;
		
		if(Main.verbose)
			System.out.println("TEST@getTierInfo: Got answerHash = "+ answerHash);
		
		return answerHash;
	}
	
	
	public static boolean nukeTier(int userId, String projID, String rewID){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("projID", projID);
		requestHash.put("rewID", rewID);
		
		if(Main.verbose)
			System.out.println("TEST@nukeTier: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.remove_meta, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@nukeTier: Now getting deletion confirmation");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("delete"));
		
		if(Main.verbose)
			System.out.println("TEST@nukeTier: Got deletion status iAnswer = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static boolean addPledge(int userId, String rewID, String ammount, String voteId){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("rewID", rewID);
		requestHash.put("ammount", ammount);
		requestHash.put("voteId", voteId);
		
		if(Main.verbose)
			System.out.println("TEST@addPledge: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.pledge_project, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@addPledge: Now getting pledge confirmation");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("pledgeConfirm"));
		
		if(Main.verbose)
			System.out.println("TEST@addPledge: Got pledgeConfirm status iAnswer = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static boolean changePledge(int userId, String rewID, String ammount){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("rewID", rewID);
		requestHash.put("ammount", ammount);
		
		if(Main.verbose)
			System.out.println("TEST@changePledge: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.change_pledge, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@changePledge: Now getting confirmation of pledge change");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("pledgeChangeConfirm"));
		
		if(Main.verbose)
			System.out.println("TEST@changePledge: Got pledgeConfirm status pledgeChangeConfirm = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static boolean removePledge(int userId, String rewID){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("rewID", rewID);
		
		if(Main.verbose)
			System.out.println("TEST@removePledge: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.remove_pledge, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@removePledge: Now getting confirmation of pledge remove");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("pledgeRemoveConfirm"));
		
		if(Main.verbose)
			System.out.println("TEST@removePledge: Got pledgeConfirm status pledgeRemoveConfirm = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static Menu_list getVoteOptions(String projID){
		Menu_list answer = null;
		int startIndex = 1;
		String[] strListRaw = null;
		String[] idListRaw = null;
		
		Hashtable<String, String> menuHash = new Hashtable<String, String>();
		
		menuHash.put("projId", projID);
		
		if(Main.verbose)
			System.out.println("TEST@getVoteOptions: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(0, operationtype.see_vote_options, menuHash);
		strListRaw = comIn.menuList.menuString;
		idListRaw = comIn.menuList.menuID;
		
		if(Main.verbose)
			System.out.println("TEST@getVoteOptions: Got the vote options.");
		
		String[] strList = formatStringArray(strListRaw, startIndex);
		strList[0] = "\t0. Return to Project menu";
		String[] idList = formatIdArray(idListRaw, startIndex);
		idList[0] = "It's time to get schwifty.";
		
		answer = new Menu_list(strList, idList);
		
		return answer;
	}
	
	public static String[] getVoteResults(String projID){
		String[] titles = null;
		String[] counts = null;
		String[] result = null;
		
		Hashtable<String, String> menuHash = new Hashtable<String, String>();
		
		menuHash.put("projId", projID);
		
		if(Main.verbose)
			System.out.println("TEST@getVoteOptions: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(0, operationtype.see_vote_results, menuHash);
		titles = comIn.menuList.menuString;
		counts = comIn.menuList.menuID;
		
		if(Main.verbose)
			System.out.println("TEST@getVoteOptions: Got the vote options.");
		
		result = parseVoteResults(titles, counts);
		
		return result;
	}
	
	private static String[] parseVoteResults(String[] titles, String[] counts){
		int total = titles.length;
		int i = 0;
		String[] strList = new String[total];
		
		for(i =0; i < total; i++){
			strList[i] = titles[i].concat(" - ").concat(counts[i]);
		}
		
		return strList;
	}
	
	public static Menu_list getMyRewards(int userId){
		Menu_list answer = null;
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		
		if(Main.verbose)
			System.out.println("TEST@getMyRewards: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.see_my_reward, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@getMyRewards: Got my rewards list.");
		
		answer = comIn.menuList;
		
		return answer;
	}
	
	public static Menu_list getMessageBoard(String projId){
		Menu_list answer = null;
		int startIndex = 2;
		String[] strListRaw = null;
		String[] idListRaw = null;
		
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("projId", projId);
		
		if(Main.verbose)
			System.out.println("TEST@getMessageBoard: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(0, operationtype.see_proj_responses, requestHash);
		strListRaw = comIn.menuList.menuString;
		idListRaw = comIn.menuList.menuID;
		
		if(Main.verbose)
			System.out.println("TEST@getMessageBoard: Got the message board.");
		
		String[] strList = formatStringArray(strListRaw, startIndex);
		strList[0] = "\t0. Return to Project menu";
		strList[1] = "\t1. Create a Message";
		String[] idList = formatIdArray(idListRaw, startIndex);
		idList[0] = "Let's just see where this goes...";
		idList[1] = "Let's get RIGGITY WRECKED";
		
		answer = new Menu_list(strList, idList);
		
		return answer;
	}
	
	public static Hashtable< String, String> getNotification(int userId, String notId){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		Hashtable<String, String> resultHash = null;
		
		requestHash.put("notId", notId);
		
		if(Main.verbose)
			System.out.println("TEST@getNotification: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.get_notification, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@getNotification: Now getting Message info");
		
		resultHash = comIn.elements;
		
		if(Main.verbose)
			System.out.println("TEST@getNotification: Got resultHash: " + resultHash);
		
		return resultHash;
	}
	
	public static boolean createNotification(int userId, String projId, String title, String descri){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("projId", projId);
		requestHash.put("title", title);
		requestHash.put("descri", descri);
		
		if(Main.verbose)
			System.out.println("TEST@createNotification: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.create_message, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@createNotification: Now getting message thread creation confirmation");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("createMessage"));
		
		if(Main.verbose)
			System.out.println("TEST@createNotification: Got createMessage = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static boolean answerNotification(int userId, String projId, String notId, String descri){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("notId", notId);
		requestHash.put("projId", projId);
		requestHash.put("descri", descri);
		
		if(Main.verbose)
			System.out.println("TEST@answerNotification: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.answer_message, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@answerNotification: Now getting reply message confirmation");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("answerMessage"));
		
		if(Main.verbose)
			System.out.println("TEST@answerNotification: Got answerMessage = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static boolean checkProjectName(String projectTitle){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("projectTitle", projectTitle);
		
		if(Main.verbose)
			System.out.println("TEST@checkProjectName: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(0, operationtype.check_proj_name, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@checkProjectName: Now checking if the server found this a worthy name.");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("projectTitle"));
		
		if(Main.verbose)
			System.out.println("TEST@checkProjectName: Got answerMessage = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static String getProjectId(String projectTitle){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("projectTitle", projectTitle);
		
		if(Main.verbose)
			System.out.println("TEST@getProjectId: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(0, operationtype.get_proj_id, requestHash);
		
		if(Main.verbose)
			System.out.println("TEST@getProjectId: Now checking if the server found project: " + projectTitle);
		
		String answer = comIn.elements.get("projId");
		
		if(Main.verbose)
			System.out.println("TEST@getProjectId: Got projId = " + answer);
		
		return answer;
	}
}
