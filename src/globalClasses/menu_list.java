package globalClasses;

import java.io.Serializable;

public class menu_list implements Serializable{
	
	public String[] menuString;
	public String[] menuID;
	
	public menu_list(String[] menuList, String[] idList){
		
		this.menuString = menuList;
		this.menuID = idList;
	}
	
}
