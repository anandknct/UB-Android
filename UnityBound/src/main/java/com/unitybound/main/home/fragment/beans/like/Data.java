package com.unitybound.main.home.fragment.beans.like;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("countMessage")
	private String countMessage;

	@SerializedName("likeStatus")
	private String likeStatus;

	public void setCountMessage(String countMessage){
		this.countMessage = countMessage;
	}

	public String getCountMessage(){
		return countMessage;
	}

	public void setLikeStatus(String likeStatus){
		this.likeStatus = likeStatus;
	}

	public String getLikeStatus(){
		return likeStatus;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"countMessage = '" + countMessage + '\'' + 
			",likeStatus = '" + likeStatus + '\'' + 
			"}";
		}
}