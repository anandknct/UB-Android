package com.unitybound.groups.beans.blockUserList;

import com.google.gson.annotations.SerializedName;

public class Group{

	@SerializedName("groupMembers")
	private int groupMembers;

	@SerializedName("groupName")
	private String groupName;

	@SerializedName("groupType")
	private String groupType;

	@SerializedName("groupDescription")
	private String groupDescription;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("groupScope")
	private String groupScope;

	@SerializedName("group_image")
	private String groupImage;

	@SerializedName("added_by")
	private String addedBy;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	public void setGroupMembers(int groupMembers){
		this.groupMembers = groupMembers;
	}

	public int getGroupMembers(){
		return groupMembers;
	}

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public String getGroupName(){
		return groupName;
	}

	public void setGroupType(String groupType){
		this.groupType = groupType;
	}

	public String getGroupType(){
		return groupType;
	}

	public void setGroupDescription(String groupDescription){
		this.groupDescription = groupDescription;
	}

	public String getGroupDescription(){
		return groupDescription;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setGroupScope(String groupScope){
		this.groupScope = groupScope;
	}

	public String getGroupScope(){
		return groupScope;
	}

	public void setGroupImage(String groupImage){
		this.groupImage = groupImage;
	}

	public String getGroupImage(){
		return groupImage;
	}

	public void setAddedBy(String addedBy){
		this.addedBy = addedBy;
	}

	public String getAddedBy(){
		return addedBy;
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

	@Override
 	public String toString(){
		return 
			"Group{" + 
			"groupMembers = '" + groupMembers + '\'' + 
			",groupName = '" + groupName + '\'' + 
			",groupType = '" + groupType + '\'' + 
			",groupDescription = '" + groupDescription + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",groupScope = '" + groupScope + '\'' + 
			",group_image = '" + groupImage + '\'' + 
			",added_by = '" + addedBy + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			"}";
		}
}