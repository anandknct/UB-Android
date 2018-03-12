package com.unitybound.groups.beans.groupComments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AllpostsItem implements Parcelable {

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
	private PostBy postBy;

	@SerializedName("post_type")
	private String postType;

	@SerializedName("discussion_type_id")
	private String discussionTypeId;

	@SerializedName("_id")
	private String id;

	@SerializedName("post_privacy")
	private String postPrivacy;

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

	public void setPostBy(PostBy postBy){
		this.postBy = postBy;
	}

	public PostBy getPostBy(){
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

	public void setCommentLike(int commentLike){
		this.commentLike = commentLike;
	}

	public int getCommentLike(){
		return commentLike;
	}

	@Override
 	public String toString(){
		return 
			"AllpostsItem{" + 
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
			",_id = '" + id + '\'' + 
			",post_privacy = '" + postPrivacy + '\'' + 
			",comment_like = '" + commentLike + '\'' + 
			"}";
		}

	public AllpostsItem() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.postImage);
		dest.writeString(this.discussionType);
		dest.writeInt(this.postComments);
		dest.writeInt(this.postShare);
		dest.writeString(this.createdAt);
		dest.writeString(this.postViewType);
		dest.writeString(this.post);
		dest.writeString(this.updatedAt);
		dest.writeParcelable(this.postBy, flags);
		dest.writeString(this.postType);
		dest.writeString(this.discussionTypeId);
		dest.writeString(this.id);
		dest.writeString(this.postPrivacy);
		dest.writeInt(this.commentLike);
	}

	protected AllpostsItem(Parcel in) {
		this.postImage = in.readString();
		this.discussionType = in.readString();
		this.postComments = in.readInt();
		this.postShare = in.readInt();
		this.createdAt = in.readString();
		this.postViewType = in.readString();
		this.post = in.readString();
		this.updatedAt = in.readString();
		this.postBy = in.readParcelable(PostBy.class.getClassLoader());
		this.postType = in.readString();
		this.discussionTypeId = in.readString();
		this.id = in.readString();
		this.postPrivacy = in.readString();
		this.commentLike = in.readInt();
	}

	public static final Creator<AllpostsItem> CREATOR = new Creator<AllpostsItem>() {
		@Override
		public AllpostsItem createFromParcel(Parcel source) {
			return new AllpostsItem(source);
		}

		@Override
		public AllpostsItem[] newArray(int size) {
			return new AllpostsItem[size];
		}
	};
}