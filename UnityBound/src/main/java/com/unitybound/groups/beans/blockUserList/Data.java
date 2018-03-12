package com.unitybound.groups.beans.blockUserList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("blockUsers")
	private List<BlockUsersItem> blockUsers;

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("group")
	private Group group;

	public void setBlockUsers(List<BlockUsersItem> blockUsers){
		this.blockUsers = blockUsers;
	}

	public List<BlockUsersItem> getBlockUsers(){
		return blockUsers;
	}

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	public void setGroup(Group group){
		this.group = group;
	}

	public Group getGroup(){
		return group;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"blockUsers = '" + blockUsers + '\'' + 
			",pageName = '" + pageName + '\'' + 
			",group = '" + group + '\'' + 
			"}";
		}
}