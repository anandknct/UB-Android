package com.unitybound.groups.beans.groupPhotos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("photos")
	private List<String> photos;

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("group")
	private Group group;

	public void setPhotos(List<String> photos){
		this.photos = photos;
	}

	public List<String> getPhotos(){
		return photos;
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
			"photos = '" + photos + '\'' + 
			",pageName = '" + pageName + '\'' + 
			",group = '" + group + '\'' + 
			"}";
		}
}