package FundstarterClient;

import java.net.*;
import java.util.Hashtable;
import java.io.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import globalClasses.Com_object;
import globalClasses.menu_list;
import globalClasses.Com_object.operationtype;

public class tcpClient {
	
	static boolean verbose = true;
	static Socket objsock = null;
	static ObjectOutputStream oos;
	static ObjectInputStream ois;
	
	public static void createTcpSocket() {
		
		int serversocket = 6000;
		
		try {
			// First open the socket
			objsock = new Socket("localhost", serversocket);
			if(verbose)
				System.out.println("SOCKET=" + objsock);
			
			// Now open the object streams
			oos = new ObjectOutputStream(objsock.getOutputStream());
			ois = new ObjectInputStream(objsock.getInputStream());
		} catch (UnknownHostException e){System.out.println("Sock:" + e.getMessage());
		} catch (IOException e) {System.out.println("IO:" + e.getMessage());
		} 
	}
	
	public static void closeTcpSocket(){
		if (objsock != null)
			try {
				objsock.close();
			} catch (IOException e) {
			    System.out.println("close:" + e.getMessage());
			}
	}
	
	private static Com_object sendThroughSocket(int userId, operationtype operation, Hashtable<String, String> content){
		Com_object comIn = null;
		// Let's create the object
		Com_object comOut = new Com_object(userId, operation, content);
		// And now send it
		try{
			oos.writeObject(comOut);
			// Better clean the socket
			oos.flush();
			// And now read
			comIn = (Com_object) ois.readObject();
		} catch (IOException e) {System.out.println("IO:" + e.getMessage());
		} catch (ClassNotFoundException e) {System.out.println("Wrong Object:" + e.getMessage());
		}
		
		return comIn;
	}
	
	public static int loginUser(String username, String password){
		int userId = 0;
		Hashtable<String, String> loginData = new Hashtable<String, String>();
		
		loginData.put("username", username);
		loginData.put("password", password);
		
		if(verbose){
			System.out.println("TEST@loginUser: Sending hash = " );
			System.out.println(loginData);
			System.out.println("TEST@loginUser: Sending everything now.");
		}
		Com_object comIn = sendThroughSocket(userId, operationtype.login, loginData);
		
		if(verbose)
			System.out.println("TEST@loginUser: Now getting userID");
		
		userId = Integer.parseInt(comIn.elements.get("userid"));
		
		if(verbose)
			System.out.println("TEST@loginUser: Got userID = "+ userId);
		
		return userId;
	}
	
	public static boolean registerUser(String username, String password){
		int userId = 0;
		int iStatus;
		Hashtable<String, String> loginData = new Hashtable<String, String>();
		
		loginData.put("username", username);
		loginData.put("password", password);
		
		if(verbose){
			System.out.println("TEST@registerUser: Sending hash = " );
			System.out.println(loginData);
			System.out.println("TEST@registerUser: Sending everything now.");
		}
		Com_object comIn = sendThroughSocket(userId, operationtype.register, loginData);
		
		if(verbose){
			System.out.println("TEST@registerUser: Now getting success status");
			System.out.println("TEST@registerUser: Got the hastable: " + comIn.elements);
		}
		
		String status = comIn.elements.get("status");
		iStatus = Integer.parseInt(status);
		
		if(verbose)
			System.out.println("TEST@registerUser: Got iStatus = "+ iStatus);
		
		if(iStatus == 0)
			return true;
		return false;
	}
	
	
	public static String checkWallet(int userId){
		String walletAmmount = null;
		Hashtable<String, String> identification = new Hashtable<String, String>();
		
		identification.put("userId", Integer.toString(userId));
		
		if(verbose)
			System.out.println("TEST@getWallet: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.check_wallet, identification);
		
		if(verbose)
			System.out.println("TEST@getWallet: Now getting wallet ammount");
		
		walletAmmount = comIn.elements.get("wallet");
		
		if(verbose)
			System.out.println("TEST@getWallet: Got walletAmmount = "+ walletAmmount);
		
		return walletAmmount;
	}
	
	public static boolean addMoneyWallet(int userId, String money){
		int mStatus;
		Hashtable<String, String> moneyHash = new Hashtable<String, String>();
		
		moneyHash.put("userId", Integer.toString(userId));
		moneyHash.put("userId", money);
		
		if(verbose)
			System.out.println("TEST@addMoneyWallet: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.add_wallet, moneyHash);
		
		if(verbose)
			System.out.println("TEST@addMoneyWallet: Now getting wallet update confirmation");
		
		mStatus = Integer.parseInt(comIn.elements.get("money"));
		
		if(verbose)
			System.out.println("TEST@addMoneyWallet: Got mStatus = "+ mStatus);
		
		if(mStatus == 0)
			return true;
		return false;
	}
	
	public static boolean createProject(int userId, String projName, String description,
										String endDate, String reqAmmount){
		int status = 0;
		Hashtable<String, String> projectHash = new Hashtable<String, String>();
		
		projectHash.put("userId", Integer.toString(userId));
		projectHash.put("projName", projName);
		projectHash.put("description", description);
		projectHash.put("endDate", endDate);
		projectHash.put("reqAmmount", reqAmmount);
		
		if(verbose)
			System.out.println("TEST@createProject: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.create_proj, projectHash);
		
		if(verbose)
			System.out.println("TEST@createProject: Now getting project creation confirmation");
		
		status = Integer.parseInt(comIn.elements.get("projectStatus"));
		
		if(verbose)
			System.out.println("TEST@createProject: Got status = "+ status);
		
		if(status == 0)
			return true;
		return false;
	}
	
	public static boolean createTier(int userId, String description, String reqAmmount){
		int status = 0;
		Hashtable<String, String> tierHash = new Hashtable<String, String>();
		
		tierHash.put("userId", Integer.toString(userId));
		tierHash.put("description", description);
		tierHash.put("reqAmmount", reqAmmount);
		
		if(verbose)
			System.out.println("TEST@createTier: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.add_meta, tierHash);
		
		if(verbose)
			System.out.println("TEST@createTier: Now getting tier creation confirmation");
		
		status = Integer.parseInt(comIn.elements.get("tierStatus"));
		
		if(verbose)
			System.out.println("TEST@createTier: Got status = "+ status);
		
		if(status == 0)
			return true;
		return false;
	}
	
	
	public static menu_list getProjectsList(boolean oldFlag){
		menu_list answer = null;
		int startIndex = 1;
		String[] strListRaw = null;
		String[] idListRaw = null;
		
		Hashtable<String, String> menuHash = new Hashtable<String, String>();
		
		if(verbose)
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
		
		if(verbose)
			System.out.println("TEST@getProjectsList: Got the menu options.");
		
		String[] strList = formatStringArray(strListRaw, startIndex);
		strList[0] = "\t0. Main menu";
		String[] idList = formatIdArray(idListRaw, startIndex);
		idList[0] = "Wubba lubba dub dub";
		
		answer = new menu_list(strList, idList);
		
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
		
		if(verbose)
			System.out.println("TEST@getProjectData: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.see_proj, requestHash);
		
		if(verbose)
			System.out.println("TEST@getProjectData: Now getting project info");
		
		answerHash = comIn.elements;
		
		if(verbose)
			System.out.println("TEST@getProjectData: Got answerHash = "+ answerHash);
		
		return answerHash;
	}
	
	public static boolean checkAdmin(int userId, String projId){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("projId", projId);
		
		if(verbose)
			System.out.println("TEST@checkAdmin: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.check_admin, requestHash);
		
		if(verbose)
			System.out.println("TEST@checkAdmin: Now getting admin status confirmation");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("admin"));
		
		if(verbose)
			System.out.println("TEST@checkAdmin: Got Admin status iAnswer = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static boolean nukeProject(int userId, String projID){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("projID", projID);
		
		if(verbose)
			System.out.println("TEST@nukeProject: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.cancel_project, requestHash);
		
		if(verbose){
			System.out.println("TEST@nukeProject: Now getting deletion confirmation");
			System.out.println("TEST@nukeProject: Got the hash: "+comIn.elements);
		}
		
		int iAnswer = Integer.parseInt(comIn.elements.get("delete"));
		
		if(verbose)
			System.out.println("TEST@nukeProject: Got deletion status iAnswer = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static menu_list getRewardsMenu(String projID){
		menu_list answer = null;
		int startIndex = 1;
		String[] strListRaw = null;
		String[] idListRaw = null;
		
		Hashtable<String, String> menuHash = new Hashtable<String, String>();
		
		if(verbose)
			System.out.println("TEST@getRewardsMenu: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(0, operationtype.see_all_rewards, menuHash);
		strListRaw = comIn.menuList.menuString;
		idListRaw = comIn.menuList.menuID;
		
		if(verbose)
			System.out.println("TEST@getRewardsMenu: Got the menu options.");
		
		String[] strList = formatStringArray(strListRaw, startIndex);
		strList[0] = "\t0. Previous menu";
		String[] idList = formatIdArray(idListRaw, startIndex);
		idList[0] = "WHATUP MY GLIP GLOPS";
		
		answer = new menu_list(strList, idList);
		
		return answer;
	}
	
	public static Hashtable< String, String> getTierInfo(int userId, String projID, String rewID){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		Hashtable<String, String> answerHash;
		
		requestHash.put("projId", projID);
		requestHash.put("rewID", rewID);
		
		if(verbose)
			System.out.println("TEST@getTierInfo: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.see_reward, requestHash);
		
		if(verbose)
			System.out.println("TEST@getTierInfo: Now getting tier info");
		
		answerHash = comIn.elements;
		
		if(verbose)
			System.out.println("TEST@getTierInfo: Got answerHash = "+ answerHash);
		
		return answerHash;
	}
	
	
	public static boolean nukeTier(int userId, String projID, String rewID){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("projID", projID);
		requestHash.put("rewID", rewID);
		
		if(verbose)
			System.out.println("TEST@nukeTier: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.remove_meta, requestHash);
		
		if(verbose)
			System.out.println("TEST@nukeTier: Now getting deletion confirmation");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("delete"));
		
		if(verbose)
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
		
		if(verbose)
			System.out.println("TEST@addPledge: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.pledge_project, requestHash);
		
		if(verbose)
			System.out.println("TEST@addPledge: Now getting pledge confirmation");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("pledgeConfirm"));
		
		if(verbose)
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
		
		if(verbose)
			System.out.println("TEST@changePledge: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.change_pledge, requestHash);
		
		if(verbose)
			System.out.println("TEST@changePledge: Now getting confirmation of pledge change");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("pledgeChangeConfirm"));
		
		if(verbose)
			System.out.println("TEST@changePledge: Got pledgeConfirm status pledgeChangeConfirm = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static boolean removePledge(int userId, String rewID){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("rewID", rewID);
		
		if(verbose)
			System.out.println("TEST@removePledge: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.remove_pledge, requestHash);
		
		if(verbose)
			System.out.println("TEST@removePledge: Now getting confirmation of pledge remove");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("pledgeRemoveConfirm"));
		
		if(verbose)
			System.out.println("TEST@removePledge: Got pledgeConfirm status pledgeRemoveConfirm = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static menu_list getVoteOptions(String projID){
		menu_list answer = null;
		int startIndex = 1;
		String[] strListRaw = null;
		String[] idListRaw = null;
		
		Hashtable<String, String> menuHash = new Hashtable<String, String>();
		
		if(verbose)
			System.out.println("TEST@getVoteOptions: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(0, operationtype.see_vote_options, menuHash);
		strListRaw = comIn.menuList.menuString;
		idListRaw = comIn.menuList.menuID;
		
		if(verbose)
			System.out.println("TEST@getVoteOptions: Got the vote options.");
		
		String[] strList = formatStringArray(strListRaw, startIndex);
		strList[0] = "\t0. Return to Project menu";
		String[] idList = formatIdArray(idListRaw, startIndex);
		idList[0] = "It's time to get schwifty.";
		
		answer = new menu_list(strList, idList);
		
		return answer;
	}
	
	public static menu_list getMyRewards(int userId){
		menu_list answer = null;
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		
		if(verbose)
			System.out.println("TEST@getMyRewards: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.see_my_reward, requestHash);
		
		if(verbose)
			System.out.println("TEST@getMyRewards: Got my rewards list.");
		
		answer = comIn.menuList;
		
		return answer;
	}
	
	public static menu_list getMessageBoard(String projId){
		menu_list answer = null;
		int startIndex = 2;
		String[] strListRaw = null;
		String[] idListRaw = null;
		
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("projId", projId);
		
		if(verbose)
			System.out.println("TEST@getMessageBoard: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(0, operationtype.see_proj_responses, requestHash);
		strListRaw = comIn.menuList.menuString;
		idListRaw = comIn.menuList.menuID;
		
		if(verbose)
			System.out.println("TEST@getMessageBoard: Got the message board.");
		
		String[] strList = formatStringArray(strListRaw, startIndex);
		strList[0] = "\t0. Return to Project menu";
		strList[1] = "\t1. Create a Message";
		String[] idList = formatIdArray(idListRaw, startIndex);
		idList[0] = "Let's just see where this goes...";
		idList[1] = "Let's get RIGGITY WRECKED";
		
		answer = new menu_list(strList, idList);
		
		return answer;
	}
	
	public static Hashtable< String, String> getNotification(int userId, String notID){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		Hashtable<String, String> resultHash = null;
		
		requestHash.put("notID", notID);
		
		if(verbose)
			System.out.println("TEST@getNotification: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.get_notification, requestHash);
		
		if(verbose)
			System.out.println("TEST@getNotification: Now getting Message info");
		
		resultHash = comIn.elements;
		
		return resultHash;
	}
	
	public static boolean createNotification(int userId, String projId, String title, String descri){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("projId", projId);
		requestHash.put("title", title);
		requestHash.put("descri", descri);
		
		if(verbose)
			System.out.println("TEST@createNotification: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.create_message, requestHash);
		
		if(verbose)
			System.out.println("TEST@createNotification: Now getting message thread creation confirmation");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("createMessage"));
		
		if(verbose)
			System.out.println("TEST@createNotification: Got createMessage = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
	public static boolean answerNotification(int userId, String projId, String descri){
		Hashtable<String, String> requestHash = new Hashtable<String, String>();
		
		requestHash.put("userId", Integer.toString(userId));
		requestHash.put("projId", projId);
		requestHash.put("descri", descri);
		
		if(verbose)
			System.out.println("TEST@answerNotification: Sending everything now.");
		
		Com_object comIn = sendThroughSocket(userId, operationtype.answer_message, requestHash);
		
		if(verbose)
			System.out.println("TEST@answerNotification: Now getting reply message confirmation");
		
		int iAnswer = Integer.parseInt(comIn.elements.get("answerMessage"));
		
		if(verbose)
			System.out.println("TEST@answerNotification: Got answerMessage = "+ iAnswer);
		
		if(iAnswer == 0)
			return true;
		return false;
	}
	
}
