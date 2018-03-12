package com.unitybound.main.friendrequest.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("friendRequestList")
	private List<FriendRequestListItem> friendRequestList;

	@SerializedName("friendRequestCount")
	private int friendRequestCount;

	public void setFriendRequestList(List<FriendRequestListItem> friendRequestList){
		this.friendRequestList = friendRequestList;
	}

	public List<FriendRequestListItem> getFriendRequestList(){
		return friendRequestList;
	}

	public void setFriendRequestCount(int friendRequestCount){
		this.friendRequestCount = friendRequestCount;
	}

	public int getFriendRequestCount(){
		return friendRequestCount;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"friendRequestList = '" + friendRequestList + '\'' + 
			",friendRequestCount = '" + friendRequestCount + '\'' + 
			"}";
		}
}