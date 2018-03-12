package com.unitybound.church.beans.libraryListMode;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("churchDocument")
	private List<ChurchDocumentItem> churchDocument;

	@SerializedName("church")
	private Church church;

	@SerializedName("pageName")
	private String pageName;

	public void setChurchDocument(List<ChurchDocumentItem> churchDocument){
		this.churchDocument = churchDocument;
	}

	public List<ChurchDocumentItem> getChurchDocument(){
		return churchDocument;
	}

	public void setChurch(Church church){
		this.church = church;
	}

	public Church getChurch(){
		return church;
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
			"churchDocument = '" + churchDocument + '\'' + 
			",church = '" + church + '\'' + 
			",pageName = '" + pageName + '\'' + 
			"}";
		}
}