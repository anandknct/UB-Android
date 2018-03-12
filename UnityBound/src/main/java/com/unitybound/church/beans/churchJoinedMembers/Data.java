package com.unitybound.church.beans.churchJoinedMembers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("church")
	private Church church;

	@SerializedName("churchUserDetails")
	private List<ChurchUserDetailsItem> churchUserDetails;

	@SerializedName("pageName")
	private String pageName;

	public void setChurch(Church church){
		this.church = church;
	}

	public Church getChurch(){
		return church;
	}

	public void setChurchUserDetails(List<ChurchUserDetailsItem> churchUserDetails){
		this.churchUserDetails = churchUserDetails;
	}

	public List<ChurchUserDetailsItem> getChurchUserDetails(){
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