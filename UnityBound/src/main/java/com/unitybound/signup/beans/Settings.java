package com.unitybound.signup.beans;

import com.google.gson.annotations.SerializedName;

public class Settings{

	@SerializedName("default_filter")
	private String defaultFilter;

	@SerializedName("default_post_creation_type")
	private String defaultPostCreationType;

	@SerializedName("who_can_see_my_post")
	private String whoCanSeeMyPost;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("who_can_look_up_post")
	private String whoCanLookUpPost;

	@SerializedName("use_language")
	private String useLanguage;

	@SerializedName("who_can_look_up_photo")
	private String whoCanLookUpPhoto;

	@SerializedName("know_language")
	private String knowLanguage;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("_id")
	private String id;

	@SerializedName("post_privacy")
	private String postPrivacy;

	@SerializedName("default_view")
	private String defaultView;

	@SerializedName("who_can_see_my_photo")
	private String whoCanSeeMyPhoto;

	public void setDefaultFilter(String defaultFilter){
		this.defaultFilter = defaultFilter;
	}

	public String getDefaultFilter(){
		return defaultFilter;
	}

	public void setDefaultPostCreationType(String defaultPostCreationType){
		this.defaultPostCreationType = defaultPostCreationType;
	}

	public String getDefaultPostCreationType(){
		return defaultPostCreationType;
	}

	public void setWhoCanSeeMyPost(String whoCanSeeMyPost){
		this.whoCanSeeMyPost = whoCanSeeMyPost;
	}

	public String getWhoCanSeeMyPost(){
		return whoCanSeeMyPost;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setWhoCanLookUpPost(String whoCanLookUpPost){
		this.whoCanLookUpPost = whoCanLookUpPost;
	}

	public String getWhoCanLookUpPost(){
		return whoCanLookUpPost;
	}

	public void setUseLanguage(String useLanguage){
		this.useLanguage = useLanguage;
	}

	public String getUseLanguage(){
		return useLanguage;
	}

	public void setWhoCanLookUpPhoto(String whoCanLookUpPhoto){
		this.whoCanLookUpPhoto = whoCanLookUpPhoto;
	}

	public String getWhoCanLookUpPhoto(){
		return whoCanLookUpPhoto;
	}

	public void setKnowLanguage(String knowLanguage){
		this.knowLanguage = knowLanguage;
	}

	public String getKnowLanguage(){
		return knowLanguage;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setPostPrivacy(String postPrivacy){
		this.postPrivacy = postPrivacy;
	}

	public String getPostPrivacy(){
		return postPrivacy;
	}

	public void setDefaultView(String defaultView){
		this.defaultView = defaultView;
	}

	public String getDefaultView(){
		return defaultView;
	}

	public void setWhoCanSeeMyPhoto(String whoCanSeeMyPhoto){
		this.whoCanSeeMyPhoto = whoCanSeeMyPhoto;
	}

	public String getWhoCanSeeMyPhoto(){
		return whoCanSeeMyPhoto;
	}

	@Override
 	public String toString(){
		return 
			"Settings{" + 
			"default_filter = '" + defaultFilter + '\'' + 
			",default_post_creation_type = '" + defaultPostCreationType + '\'' + 
			",who_can_see_my_post = '" + whoCanSeeMyPost + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",who_can_look_up_post = '" + whoCanLookUpPost + '\'' + 
			",use_language = '" + useLanguage + '\'' + 
			",who_can_look_up_photo = '" + whoCanLookUpPhoto + '\'' + 
			",know_language = '" + knowLanguage + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",_id = '" + id + '\'' + 
			",post_privacy = '" + postPrivacy + '\'' + 
			",default_view = '" + defaultView + '\'' + 
			",who_can_see_my_photo = '" + whoCanSeeMyPhoto + '\'' + 
			"}";
		}
}