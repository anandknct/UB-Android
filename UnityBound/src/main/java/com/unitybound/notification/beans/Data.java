package com.unitybound.notification.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("notificationList")
	private List<NotificationListItem> notificationList;

	@SerializedName("notificationCount")
	private int notificationCount;

	public void setNotificationList(List<NotificationListItem> notificationList){
		this.notificationList = notificationList;
	}

	public List<NotificationListItem> getNotificationList(){
		return notificationList;
	}

	public void setNotificationCount(int notificationCount){
		this.notificationCount = notificationCount;
	}

	public int getNotificationCount(){
		return notificationCount;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"notificationList = '" + notificationList + '\'' + 
			",notificationCount = '" + notificationCount + '\'' + 
			"}";
		}
}