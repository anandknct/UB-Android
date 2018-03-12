package com.unitybound.church.beans.detailMembers;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data{

	@SerializedName("church")
	private Church church;

	@SerializedName("churchUserDetails")
	private ArrayList<ChurchUserDetailsItem> churchUserDetails;

	@SerializedName("pageName")
	private String pageName;

	public void setChurch(Church church){
		this.church = church;
	}

	public Church getChurch(){
		return church;
	}

	public void setChurchUserDetails(ArrayList<ChurchUserDetailsItem> churchUserDetails){
		this.churchUserDetails = churchUserDetails;
	}

	public ArrayList<ChurchUserDetailsItem> getChurchUserDetails(){
		return churchUserDetails;
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
			"church = '" + church + '\'' + 
			",churchUserDetails = '" + churchUserDetails + '\'' + 
			",pageName = '" + pageName + '\'' + 
			"}";
		}
}