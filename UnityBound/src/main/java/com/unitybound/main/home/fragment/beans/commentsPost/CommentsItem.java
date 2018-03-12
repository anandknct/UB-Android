package com.unitybound.main.home.fragment.beans.commentsPost;

import com.google.gson.annotations.SerializedName;

public class CommentsItem{

	@SerializedName("comment_by")
	private CommentBy commentBy;

	@SerializedName("commment_to")
	private String commmentTo;

	@SerializedName("comments")
	private String comments;

	@SerializedName("post_id")
	private String postId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("comment_image")
	private String commentImage;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	@SerializedName("comment_like")
	private int commentLike;

	private boolean isLikeLocal = false;

	public boolean isLikeLocal() {
		return isLikeLocal;
	}

	public void setLikeLocal(boolean likeLocal) {
		isLikeLocal = likeLocal;
	}

	public void setCommentBy(CommentBy commentBy){
		this.commentBy = commentBy;
	}

	public CommentBy getCommentBy(){
		return commentBy;
	}

	public void setCommmentTo(String commmentTo){
		this.commmentTo = commmentTo;
	}

	public String getCommmentTo(){
		return commmentTo;
	}

	public void setComments(String comments){
		this.comments = comments;
	}

	public String getComments(){
		return comments;
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

	public void setCommentImage(String commentImage){
		this.commentImage = commentImage;
	}

	public String getCommentImage(){
		return commentImage;
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

	public void setCommentLike(int commentLike){
		this.commentLike = commentLike;
	}

	public int getCommentLike(){
		return commentLike;
	}

	@Override
 	public String toString(){
		return 
			"CommentsItem{" + 
			"comment_by = '" + commentBy + '\'' + 
			",commment_to = '" + commmentTo + '\'' + 
			",comments = '" + comments + '\'' + 
			",post_id = '" + postId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",comment_image = '" + commentImage + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			",comment_like = '" + commentLike + '\'' + 
			"}";
		}
}