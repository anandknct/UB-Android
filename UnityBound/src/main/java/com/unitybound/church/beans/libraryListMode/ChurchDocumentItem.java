package com.unitybound.church.beans.libraryListMode;

import com.google.gson.annotations.SerializedName;

public class ChurchDocumentItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("church_id")
	private String churchId;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	@SerializedName("documentName")
	private String documentName;

	@SerializedName("documentFile")
	private String documentFile;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setChurchId(String churchId){
		this.churchId = churchId;
	}

	public String getChurchId(){
		return churchId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setDocumentName(String documentName){
		this.documentName = documentName;
	}

	public String getDocumentName(){
		return documentName;
	}

	public void setDocumentFile(String documentFile){
		this.documentFile = documentFile;
	}

	public String getDocumentFile(){
		return documentFile;
	}

	@Override
 	public String toString(){
		return 
			"ChurchDocumentItem{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",church_id = '" + churchId + '\'' + 
			",user_id = '" + userId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			",documentName = '" + documentName + '\'' + 
			",documentFile = '" + documentFile + '\'' + 
			"}";
		}
}