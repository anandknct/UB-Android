package com.unitybound.main.home.fragment.beans.homeFeeds;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllPostResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("page_no")
	private int pageNo;

	@SerializedName("allposts")
	private ArrayList<AllPostsItem> allposts;

	@SerializedName("status")
	private String status;

	@SerializedName("notificationCount")
	private int notificationCount;

	@SerializedName("friendRequestCount")
	private int friendRequestCount;

	public int getNotificationCount() {
		return notificationCount;
	}

	public void setNotificationCount(int notificationCount) {
		this.notificationCount = notificationCount;
	}

	public int getFriendRequestCount() {
		return friendRequestCount;
	}

	public void setFriendRequestCount(int friendRequestCount) {
		this.friendRequestCount = friendRequestCount;
	}

	public void setStatuscode(String statuscode){
		this.statuscode = statuscode;
	}

	public String getStatuscode(){
		return statuscode;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setPageNo(int pageNo){
		this.pageNo = pageNo;
	}

	public int getPageNo(){
		return pageNo;
	}

	public void setAllposts(ArrayList<AllPostsItem> allposts){
		this.allposts = allposts;
	}

	public ArrayList<AllPostsItem> getAllposts(){
		return allposts;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"AllPostResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",page_no = '" + pageNo + '\'' + 
			",allposts = '" + allposts + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}