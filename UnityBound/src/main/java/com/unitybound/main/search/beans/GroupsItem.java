package com.unitybound.main.search.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupsItem{

	@SerializedName("groupSco D/OkHttp: pe")
	private String groupScoDOkHttpPe;

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

	@SerializedName("added_by")
	private String addedBy;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	@Expose
	@SerializedName("group_image")
	private String group_image;

	public String getGroup_image() {
		return group_image;
	}

	public void setGroup_image(String group_image) {
		this.group_image = group_image;
	}

	public void setGroupScoDOkHttpPe(String groupScoDOkHttpPe){
		this.groupScoDOkHttpPe = groupScoDOkHttpPe;
	}

	public String getGroupScoDOkHttpPe(){
		return groupScoDOkHttpPe;
	}

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
			"GroupsItem{" + 
			"groupSco D/OkHttp: pe = '" + groupScoDOkHttpPe + '\'' + 
			",groupMembers = '" + groupMembers + '\'' + 
			",groupName = '" + groupName + '\'' + 
			",groupType = '" + groupType + '\'' + 
			",groupDescription = '" + groupDescription + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",added_by = '" + addedBy + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			"}";
		}
}