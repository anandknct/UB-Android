package com.unitybound.signup.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("filterArea")
	private List<String> filterArea;

	@SerializedName("notificationCount")
	private int notificationCount;

	@SerializedName("postType")
	private List<String> postType;

	@SerializedName("friendRequestCount")
	private int friendRequestCount;

	@SerializedName("user")
	private User user;

	@SerializedName("allposts")
	private List<Object> allposts;

	public void setFilterArea(List<String> filterArea){
		this.filterArea = filterArea;
	}

	public List<String> getFilterArea(){
		return filterArea;
	}

	public void setNotificationCount(int notificationCount){
		this.notificationCount = notificationCount;
	}

	public int getNotificationCount(){
		return notificationCount;
	}

	public void setPostType(List<String> postType){
		this.postType = postType;
	}

	public List<String> getPostType(){
		return postType;
	}

	public void setFriendRequestCount(int friendRequestCount){
		this.friendRequestCount = friendRequestCount;
	}

	public int getFriendRequestCount(){
		return friendRequestCount;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setAllposts(List<Object> allposts){
		this.allposts = allposts;
	}

	public List<Object> getAllposts(){
		return allposts;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"filterArea = '" + filterArea + '\'' + 
			",notificationCount = '" + notificationCount + '\'' + 
			",postType = '" + postType + '\'' + 
			",friendRequestCount = '" + friendRequestCount + '\'' + 
			",user = '" + user + '\'' + 
			",allposts = '" + allposts + '\'' + 
			"}";
		}
}