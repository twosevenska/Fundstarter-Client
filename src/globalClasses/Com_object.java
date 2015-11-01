package globalClasses;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.UUID;

public class Com_object implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String idpackage;//package id, to avoid repeated packages
	public operationtype op;//type of operation
	public Hashtable<String, String> elements;//elements of the package
	public Menu_list menuList;//Optional, easy way to get the menu options
    
	public Com_object(int id, operationtype ope,Hashtable<String, String> ele)
	{
		idpackage = generateIdPackage(id);
		elements = ele;
		op = ope;
	}
	
	public Com_object(int id, operationtype ope,Hashtable<String, String> ele, Menu_list mList)
	{
		idpackage = generateIdPackage(id);
		elements = ele;
		op = ope;
		menuList = mList;
	}
	
	public enum operationtype{
		see_all_proj_off,
		see_all_proj_on,
		see_proj_responses,
		login,
		register,
		create_proj,
		see_my_reward,
		check_query,
		pledge_project,
		see_pledges,
		see_reward,
		cancel_project,
		check_metas,
		add_meta,
		remove_meta,
		check_wallet,
		add_wallet,
		see_proj,
		check_admin,
		see_all_rewards,
		change_pledge,
		remove_pledge,
		see_vote_options,
		get_notification,
		create_message,
		answer_message,
		check_proj_name,
		see_my_projs,
		get_proj_id,
		see_vote_results
	}
	
	public String generateIdPackage(int id)//generate id for the request
	{
		return UUID.randomUUID().toString();
	}
}