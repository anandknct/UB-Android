package com.unitybound.account.beans.photos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("userInfo")
	private UserInfo userInfo;

	@SerializedName("photos")
	private List<PhotosItem> photos;

	@SerializedName("pageName")
	private String pageName;

	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}

	public UserInfo getUserInfo(){
		return userInfo;
	}

	public void setPhotos(List<PhotosItem> photos){
		this.photos = photos;
	}

	public List<PhotosItem> getPhotos(){
		return photos;
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
			",photos = '" + photos + '\'' + 
			",pageName = '" + pageName + '\'' + 
			"}";
		}
}