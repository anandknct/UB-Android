package com.unitybound.account.beans.profile;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("userInfo")
	private UserInfo userInfo;

	@SerializedName("profile")
	private Profile profile;

	@SerializedName("pageName")
	private String pageName;

	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}

	public UserInfo getUserInfo(){
		return userInfo;
	}

	public void setProfile(Profile profile){
		this.profile = profile;
	}

	public Profile getProfile(){
		return profile;
	}

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"userInfo = '" + userInfo + '\'' + 
			",profile = '" + profile + '\'' + 
			",pageName = '" + pageName + '\'' + 
			"}";
		}
}