package com.unitybound.groups.beans.joinedGroups;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("user_groups")
	private List<UserJoinedGroupsItem> userGroups;

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	public void setUserGroups(List<UserJoinedGroupsItem> userGroups){
		this.userGroups = userGroups;
	}

	public List<UserJoinedGroupsItem> getUserGroups(){
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