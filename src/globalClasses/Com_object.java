package globalClasses;

import java.util.Hashtable;

public class Com_object {
	long idpackage;//package id, to avoid repeated packages
	operationtype op;//type of operation
	Hashtable<String, String> elements;//elements of the package
    
	public Com_object(int id, operationtype ope,Hashtable<String, String> ele)
	{
		idpackage = generateIdPackage(id);
		idpackage = id;
		elements = ele;
		op = ope;
	}
	
	public enum operationtype{
		see_all_proj_off,
		see_all_proj_on,
		see_proj_responses,
		login,
		register,
		create_proj,
		see_proj_my,
		check_query,
		pledge_project,
		see_pledges,
		see_reward,
		cancel_project,
		check_metas,
		add_meta,
		remove_meta
	}
	
	public String server_call_operation()
	{
		/*RMI functions on every case*/
		switch(op)
		{
		case see_all_proj_off:
			break;
		case see_all_proj_on:
			break;
		case see_proj_responses:
			break;
		case login:
			break;
		case register:
			break;
		case create_proj:
			break;
		case see_proj_my:
			break;
		case check_query:
			break;
		case pledge_project:
			break;
		case see_pledges:
			break;
		case see_reward:
			break;
		case cancel_project:
			break;
		case check_metas:
			break;
		case add_meta:
			break;
		case remove_meta:
			break;
		}
		return "CHECK THE TYPE OF PACKAGE!";
	}
	
	public long generateIdPackage(int id)//generate id for the request
	{
		return Long.parseLong(Integer.toString(id) + Long.toString(System.currentTimeMillis()/1000));
	}
}