package com.unitybound.main.home.fragment.beans.addCommentsList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.unitybound.main.home.fragment.beans.commentsPost.CommentBy;

public class Data implements Parcelable {

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

	@SerializedName("comment_by")
	private CommentBy comment_by;

	@SerializedName("_id")
	private String id;

	@SerializedName("comment_like")
	private int commentLike;

	public CommentBy getComment_by() {
		return comment_by;
	}

	public void setComment_by(CommentBy comment_by) {
		this.comment_by = comment_by;
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
			"Data{" +
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

	public Data() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.commmentTo);
		dest.writeString(this.comments);
		dest.writeString(this.postId);
		dest.writeString(this.updatedAt);
		dest.writeString(this.commentImage);
		dest.writeString(this.createdAt);
		dest.writeParcelable(this.comment_by, flags);
		dest.writeString(this.id);
		dest.writeInt(this.commentLike);
	}

	protected Data(Parcel in) {
		this.commmentTo = in.readString();
		this.comments = in.readString();
		this.postId = in.readString();
		this.updatedAt = in.readString();
		this.commentImage = in.readString();
		this.createdAt = in.readString();
		this.comment_by = in.readParcelable(CommentBy.class.getClassLoader());
		this.id = in.readString();
		this.commentLike = in.readInt();
	}

	public static final Creator<Data> CREATOR = new Creator<Data>() {
		@Override
		public Data createFromParcel(Parcel source) {
			return new Data(source);
		}

		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
	};
}