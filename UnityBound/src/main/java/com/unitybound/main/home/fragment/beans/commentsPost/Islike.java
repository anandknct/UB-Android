package com.unitybound.main.home.fragment.beans.commentsPost;

import com.google.gson.annotations.SerializedName;

public class Islike{

	@SerializedName("like_to")
	private String likeTo;

	@SerializedName("post_id")
	private String postId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("islike")
	private int islike;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	@SerializedName("like_by")
	private String likeBy;

	public void setLikeTo(String likeTo){
		this.likeTo = likeTo;
	}

	public String getLikeTo(){
		return likeTo;
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

	public void setIslike(int islike){
		this.islike = islike;
	}

	public int getIslike(){
		return islike;
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

	public void setLikeBy(String likeBy){
		this.likeBy = likeBy;
	}

	public String getLikeBy(){
		return likeBy;
	}

	@Override
 	public String toString(){
		return 
			"Islike{" + 
			"like_to = '" + likeTo + '\'' + 
			",post_id = '" + postId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",islike = '" + islike + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			",like_by = '" + likeBy + '\'' + 
			"}";
		}
}