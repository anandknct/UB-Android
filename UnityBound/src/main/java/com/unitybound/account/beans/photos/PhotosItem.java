package com.unitybound.account.beans.photos;

import com.google.gson.annotations.SerializedName;

public class PhotosItem{

	@SerializedName("accessType")
	private String accessType;

	@SerializedName("path")
	private String path;

	@SerializedName("post_id")
	private String postId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("addto")
	private String addto;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("post_type")
	private String postType;

	@SerializedName("_id")
	private String id;

	@SerializedName("type")
	private String type;

	@SerializedName("addby")
	private String addby;

	public void setAccessType(String accessType){
		this.accessType = accessType;
	}

	public String getAccessType(){
		return accessType;
	}

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}

	public void setPostId(String postId){
		this.postId = postId;
	}

	public String getPostId(){
		return postId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setAddto(String addto){
		this.addto = addto;
	}

	public String getAddto(){
		return addto;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPostType(String postType){
		this.postType = postType;
	}

	public String getPostType(){
		return postType;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setAddby(String addby){
		this.addby = addby;
	}

	public String getAddby(){
		return addby;
	}

	@Override
 	public String toString(){
		return 
			"PhotosItem{" + 
			"accessType = '" + accessType + '\'' + 
			",path = '" + path + '\'' + 
			",post_id = '" + postId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",addto = '" + addto + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",post_type = '" + postType + '\'' + 
			",_id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			",addby = '" + addby + '\'' + 
			"}";
		}
}