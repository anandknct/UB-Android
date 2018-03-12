package com.unitybound.main.home.fragment.beans.commentsReplyList;

import com.google.gson.annotations.SerializedName;

public class CommentsItem{

	@SerializedName("reply_comments")
	private String replyComments;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	@SerializedName("comment_id")
	private String commentId;

	@SerializedName("LikedbyMe")
	private boolean likedbyMe;

	@SerializedName("reply_comment_by")
	private ReplyCommentBy replyCommentBy;

	@SerializedName("TotalLike")
	private int totalLike;

	public void setReplyComments(String replyComments){
		this.replyComments = replyComments;
	}

	public String getReplyComments(){
		return replyComments;
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

	public void setCommentId(String commentId){
		this.commentId = commentId;
	}

	public String getCommentId(){
		return commentId;
	}

	public void setLikedbyMe(boolean likedbyMe){
		this.likedbyMe = likedbyMe;
	}

	public boolean isLikedbyMe(){
		return likedbyMe;
	}

	public void setReplyCommentBy(ReplyCommentBy replyCommentBy){
		this.replyCommentBy = replyCommentBy;
	}

	public ReplyCommentBy getReplyCommentBy(){
		return replyCommentBy;
	}

	public void setTotalLike(int totalLike){
		this.totalLike = totalLike;
	}

	public int getTotalLike(){
		return totalLike;
	}

	@Override
 	public String toString(){
		return 
			"CommentsItem{" + 
			"reply_comments = '" + replyComments + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			",comment_id = '" + commentId + '\'' + 
			",likedbyMe = '" + likedbyMe + '\'' + 
			",reply_comment_by = '" + replyCommentBy + '\'' + 
			",totalLike = '" + totalLike + '\'' + 
			"}";
		}
}