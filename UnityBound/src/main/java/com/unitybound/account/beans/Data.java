package com.unitybound.account.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("friends")
	private List<FriendsItem> friends;

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	public void setFriends(List<FriendsItem> friends){
		this.friends = friends;
	}

	public List<FriendsItem> getFriends(){
		return friends;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"pageName = '" + pageName + '\'' + 
			",friends = '" + friends + '\'' + 
			"}";
		}
}