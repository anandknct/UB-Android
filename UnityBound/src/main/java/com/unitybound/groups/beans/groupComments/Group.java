package com.unitybound.groups.beans.groupComments;

import com.google.gson.annotations.SerializedName;

public class Group{

	@SerializedName("groupMembers")
	private int groupMembers;

	@SerializedName("groupType")
	private String groupType;

	@SerializedName("group_image")
	private String groupImage;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("group_image_m")
	private String groupImageM;

	@SerializedName("group_image_l")
	private String groupImageL;

	@SerializedName("group_image_s")
	private String groupImageS;

	@SerializedName("groupName")
	private String groupName;

	@SerializedName("groupDescription")
	private String groupDescription;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("groupScope")
	private String groupScope;

	@SerializedName("added_by")
	private String addedBy;

	@SerializedName("_id")
	private String id;

	public void setGroupMembers(int groupMembers){
		this.groupMembers = groupMembers;
	}

	public int getGroupMembers(){
		return groupMembers;
	}

	public void setGroupType(String groupType){
		this.groupType = groupType;
	}

	public String getGroupType(){
		return groupType;
	}

	public void setGroupImage(String groupImage){
		this.groupImage = groupImage;
	}

	public String getGroupImage(){
		return groupImage;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setGroupImageM(String groupImageM){
		this.groupImageM = groupImageM;
	}

	public String getGroupImageM(){
		return groupImageM;
	}

	public void setGroupImageL(String groupImageL){
		this.groupImageL = groupImageL;
	}

	public String getGroupImageL(){
		return groupImageL;
	}

	public void setGroupImageS(String groupImageS){
		this.groupImageS = groupImageS;
	}

	public String getGroupImageS(){
		return groupImageS;
	}

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public String getGroupName(){
		return groupName;
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

	public void setAddedBy(String addedBy){
		this.addedBy = addedBy;
	}

	public String getAddedBy(){
		return addedBy;
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
			",groupType = '" + groupType + '\'' + 
			",group_image = '" + groupImage + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",group_image_m = '" + groupImageM + '\'' + 
			",group_image_l = '" + groupImageL + '\'' + 
			",group_image_s = '" + groupImageS + '\'' + 
			",groupName = '" + groupName + '\'' + 
			",groupDescription = '" + groupDescription + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",groupScope = '" + groupScope + '\'' + 
			",added_by = '" + addedBy + '\'' + 
			",_id = '" + id + '\'' + 
			"}";
		}
}