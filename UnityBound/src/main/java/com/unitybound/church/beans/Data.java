package com.unitybound.church.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("churches")
	private List<ChurchesItem> churches;

	@SerializedName("pageName")
	private String pageName;

	public void setChurches(List<ChurchesItem> churches){
		this.churches = churches;
	}

	public List<ChurchesItem> getChurches(){
		return churches;
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
			"churches = '" + churches + '\'' + 
			",pageName = '" + pageName + '\'' + 
			"}";
		}
}