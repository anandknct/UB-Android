package com.unitybound.groups.beans.groupMembers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Group implements Parcelable {

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
			",added_by = '" + addedBy + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.groupMembers);
		dest.writeString(this.groupName);
		dest.writeString(this.groupType);
		dest.writeString(this.groupDescription);
		dest.writeString(this.updatedAt);
		dest.writeString(this.groupScope);
		dest.writeString(this.addedBy);
		dest.writeString(this.createdAt);
		dest.writeString(this.id);
	}

	public Group() {
	}

	protected Group(Parcel in) {
		this.groupMembers = in.readInt();
		this.groupName = in.readString();
		this.groupType = in.readString();
		this.groupDescription = in.readString();
		this.updatedAt = in.readString();
		this.groupScope = in.readString();
		this.addedBy = in.readString();
		this.createdAt = in.readString();
		this.id = in.readString();
	}

	public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
		@Override
		public Group createFromParcel(Parcel source) {
			return new Group(source);
		}

		@Override
		public Group[] newArray(int size) {
			return new Group[size];
		}
	};
}