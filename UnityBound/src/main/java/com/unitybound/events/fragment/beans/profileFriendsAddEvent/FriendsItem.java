package com.unitybound.events.fragment.beans.profileFriendsAddEvent;

import com.google.gson.annotations.SerializedName;

public class FriendsItem{

	@SerializedName("country")
	private String country;

	@SerializedName("profile_image")
	private String profileImage;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("state")
	private String state;

	@SerializedName("mutualFriends")
	private int mutualFriends;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setMutualFriends(int mutualFriends){
		this.mutualFriends = mutualFriends;
	}

	public int getMutualFriends(){
		return mutualFriends;
	}

	@Override
 	public String toString(){
		return 
			"FriendsItem{" + 
			"country = '" + country + '\'' + 
			",profile_image = '" + profileImage + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			",mutualFriends = '" + mutualFriends + '\'' + 
			"}";
		}
}