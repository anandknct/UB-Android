package com.unitybound.groups.beans.groupMembers;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GroupUserDetailsItem implements Parcelable {

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("lastName")
	private String lastName;

	@SerializedName("id")
	private String id;

	@SerializedName("profileImage")
	private String profileImage;

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	@Override
 	public String toString(){
		return 
			"GroupUserDetailsItem{" + 
			"firstName = '" + firstName + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",id = '" + id + '\'' + 
			",profileImage = '" + profileImage + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firstName);
		dest.writeString(this.lastName);
		dest.writeString(this.id);
		dest.writeString(this.profileImage);
	}

	public GroupUserDetailsItem() {
	}

	protected GroupUserDetailsItem(Parcel in) {
		this.firstName = in.readString();
		this.lastName = in.readString();
		this.id = in.readString();
		this.profileImage = in.readString();
	}

	public static final Parcelable.Creator<GroupUserDetailsItem> CREATOR = new Parcelable.Creator<GroupUserDetailsItem>() {
		@Override
		public GroupUserDetailsItem createFromParcel(Parcel source) {
			return new GroupUserDetailsItem(source);
		}

		@Override
		public GroupUserDetailsItem[] newArray(int size) {
			return new GroupUserDetailsItem[size];
		}
	};
}