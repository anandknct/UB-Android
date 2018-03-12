package com.unitybound.main.my.prayer.request.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("allposts")
	private List<AllpostsItem> allposts;

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	public void setAllposts(List<AllpostsItem> allposts){
		this.allposts = allposts;
	}

	public List<AllpostsItem> getAllposts(){
		return allposts;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"pageName = '" + pageName + '\'' + 
			",allposts = '" + allposts + '\'' + 
			"}";
		}
}