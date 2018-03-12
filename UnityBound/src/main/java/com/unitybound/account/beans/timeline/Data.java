package com.unitybound.account.beans.timeline;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data{

	@SerializedName("userInfo")
	private UserInfo userInfo;

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("allposts")
	private ArrayList<AllpostsItem> allposts;

	@SerializedName("profile")
	private ProfileAboutData profileData;

	public ProfileAboutData getProfileData() {
		return profileData;
	}

	public void setProfileData(ProfileAboutData profileData) {
		this.profileData = profileData;
	}

	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}

	public UserInfo getUserInfo(){
		return userInfo;
	}

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	public void setAllposts(ArrayList<AllpostsItem> allposts){
		this.allposts = allposts;
	}

	public ArrayList<AllpostsItem> getAllposts(){
		return allposts;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"userInfo = '" + userInfo + '\'' + 
			",pageName = '" + pageName + '\'' + 
			",allposts = '" + allposts + '\'' + 
			"}";
		}
}