package com.unitybound.main.home.fragment.beans.commentsReplyList;

import com.google.gson.annotations.SerializedName;

public class ReplyCommentBy{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("profileImage")
	private String profileImage;

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

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	@Override
 	public String toString(){
		return 
			"ReplyCommentBy{" + 
			"name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",profileImage = '" + profileImage + '\'' + 
			"}";
		}
}