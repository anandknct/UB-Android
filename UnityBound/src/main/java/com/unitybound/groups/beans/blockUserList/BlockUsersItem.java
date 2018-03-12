package com.unitybound.groups.beans.blockUserList;

import com.google.gson.annotations.SerializedName;

public class BlockUsersItem{

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
			"BlockUsersItem{" + 
			"firstName = '" + firstName + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",id = '" + id + '\'' + 
			",profileImage = '" + profileImage + '\'' + 
			"}";
		}
}