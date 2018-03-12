package com.unitybound.groups.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("user_groups")
	private List<UserGroupsItem> userGroups;

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	public void setUserGroups(List<UserGroupsItem> userGroups){
		this.userGroups = userGroups;
	}

	public List<UserGroupsItem> getUserGroups(){
		return userGroups;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"pageName = '" + pageName + '\'' + 
			",user_groups = '" + userGroups + '\'' + 
			"}";
		}
}