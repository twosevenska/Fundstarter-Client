package globalClasses;

import java.io.Serializable;

public class Menu_list implements Serializable{
	
	public String[] menuString;
	public String[] menuID;
	
	public Menu_list(String[] menuList, String[] idList){
		
		this.menuString = menuList;
		this.menuID = idList;
	}
	
}
