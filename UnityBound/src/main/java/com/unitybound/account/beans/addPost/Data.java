package com.unitybound.account.beans.addPost;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("post_image")
	private String postImage;

	@SerializedName("discussion_type")
	private String discussionType;

	@SerializedName("post_comments")
	private int postComments;

	@SerializedName("post_share")
	private int postShare;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("post_view_type")
	private String postViewType;

	@SerializedName("post")
	private String post;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("post_by")
	private String postBy;

	@SerializedName("post_type")
	private String postType;

	@SerializedName("discussion_type_id")
	private String discussionTypeId;

	@SerializedName("post_privacy")
	private String postPrivacy;

	@SerializedName("_id")
	private String id;

	@SerializedName("comment_like")
	private int commentLike;

	public void setPostImage(String postImage){
		this.postImage = postImage;
	}

	public String getPostImage(){
		return postImage;
	}

	public void setDiscussionType(String discussionType){
		this.discussionType = discussionType;
	}

	public String getDiscussionType(){
		return discussionType;
	}

	public void setPostComments(int postComments){
		this.postComments = postComments;
	}

	public int getPostComments(){
		return postComments;
	}

	public void setPostShare(int postShare){
		this.postShare = postShare;
	}

	public int getPostShare(){
		return postShare;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPostViewType(String postViewType){
		this.postViewType = postViewType;
	}

	public String getPostViewType(){
		return postViewType;
	}

	public void setPost(String post){
		this.post = post;
	}

	public String getPost(){
		return post;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setPostBy(String postBy){
		this.postBy = postBy;
	}

	public String getPostBy(){
		return postBy;
	}

	public void setPostType(String postType){
		this.postType = postType;
	}

	public String getPostType(){
		return postType;
	}

	public void setDiscussionTypeId(String discussionTypeId){
		this.discussionTypeId = discussionTypeId;
	}

	public String getDiscussionTypeId(){
		return discussionTypeId;
	}

	public void setPostPrivacy(String postPrivacy){
		this.postPrivacy = postPrivacy;
	}

	public String getPostPrivacy(){
		return postPrivacy;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setCommentLike(int commentLike){
		this.commentLike = commentLike;
	}

	public int getCommentLike(){
		return commentLike;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"post_image = '" + postImage + '\'' + 
			",discussion_type = '" + discussionType + '\'' + 
			",post_comments = '" + postComments + '\'' + 
			",post_share = '" + postShare + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",post_view_type = '" + postViewType + '\'' + 
			",post = '" + post + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",post_by = '" + postBy + '\'' + 
			",post_type = '" + postType + '\'' + 
			",discussion_type_id = '" + discussionTypeId + '\'' + 
			",post_privacy = '" + postPrivacy + '\'' + 
			",_id = '" + id + '\'' + 
			",comment_like = '" + commentLike + '\'' + 
			"}";
		}
}