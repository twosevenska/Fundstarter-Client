package fundstarterClient;

import java.io.Serializable;

public class loginInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	
	public loginInfo(String  username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public String getUser(){
		return username;
	}
	
	public String getPass(){
		return password;
	}
}
