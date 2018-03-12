package com.unitybound.church.beans.churchDetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllpostsItem implements Parcelable {

	@Expose
	@SerializedName("post_like_by_me")
	private boolean isLikeLocal = false;

	private boolean isFavLocal = false;

	@Expose
	@SerializedName("post_like")
	private int postLikes = 0;

	@SerializedName("post_image")
	private String postImage;

	@SerializedName("discussion_type")
	private String discussionType;

	@SerializedName("post_preview_image")
	private String postPreviewImage;

	@SerializedName("post_comments")
	private int postComments;

	@SerializedName("post_share")
	private int postShare;

	@SerializedName("post_preview_code_url")
	private String postPreviewCodeUrl;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("post_preview_description")
	private String postPreviewDescription;

	@SerializedName("post_view_type")
	private String postViewType;

	@SerializedName("post")
	private String post;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("post_preview_url")
	private String postPreviewUrl;

	@SerializedName("post_preview_type")
	private String postPreviewType;

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

	@SerializedName("post_preview_code")
	private String postPreviewCode;

	@SerializedName("post_preview_title")
	private String postPreviewTitle;

	@SerializedName("comment_like")
	private int commentLike;

	@Expose
	@SerializedName("prayerAnswer")
	private int prayerAnswer = 0;

	@Expose
	@SerializedName("prayerAnswerComment")
	private String prayerAnswerComment;

	public int getPostLikes() {
		return postLikes;
	}

	public void setPostLikes(int postLikes) {
		this.postLikes = postLikes;
	}

	public void setPrayerAnswerComment(String prayerAnswerComment) {
		this.prayerAnswerComment = prayerAnswerComment;
	}

	public boolean isLikeLocal() {
		return isLikeLocal;
	}

	public boolean isFavLocal() {
		return isFavLocal;
	}

	public void setFavLocal(boolean favLocal) {
		isFavLocal = favLocal;
	}

	public void setLikeLocal(boolean likeLocal) {
		isLikeLocal = likeLocal;
	}

	public int getPrayerAnswer() {
		return prayerAnswer;
	}

	public void setPrayerAnswer(int prayerAnswer) {
		this.prayerAnswer = prayerAnswer;
	}

	public String getPrayerAnswerComment() {
		return prayerAnswerComment;
	}

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

	public void setPostPreviewImage(String postPreviewImage){
		this.postPreviewImage = postPreviewImage;
	}

	public String getPostPreviewImage(){
		return postPreviewImage;
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

	public void setPostPreviewCodeUrl(String postPreviewCodeUrl){
		this.postPreviewCodeUrl = postPreviewCodeUrl;
	}

	public String getPostPreviewCodeUrl(){
		return postPreviewCodeUrl;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPostPreviewDescription(String postPreviewDescription){
		this.postPreviewDescription = postPreviewDescription;
	}

	public String getPostPreviewDescription(){
		return postPreviewDescription;
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

	public void setPostPreviewUrl(String postPreviewUrl){
		this.postPreviewUrl = postPreviewUrl;
	}

	public String getPostPreviewUrl(){
		return postPreviewUrl;
	}

	public void setPostPreviewType(String postPreviewType){
		this.postPreviewType = postPreviewType;
	}

	public String getPostPreviewType(){
		return postPreviewType;
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

	public void setPostPreviewCode(String postPreviewCode){
		this.postPreviewCode = postPreviewCode;
	}

	public String getPostPreviewCode(){
		return postPreviewCode;
	}

	public void setPostPreviewTitle(String postPreviewTitle){
		this.postPreviewTitle = postPreviewTitle;
	}

	public String getPostPreviewTitle(){
		return postPreviewTitle;
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
			",post_preview_image = '" + postPreviewImage + '\'' + 
			",post_comments = '" + postComments + '\'' + 
			",post_share = '" + postShare + '\'' + 
			",post_preview_code_url = '" + postPreviewCodeUrl + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",post_preview_description = '" + postPreviewDescription + '\'' + 
			",post_view_type = '" + postViewType + '\'' + 
			",post = '" + post + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",post_preview_url = '" + postPreviewUrl + '\'' + 
			",post_preview_type = '" + postPreviewType + '\'' + 
			",post_by = '" + postBy + '\'' + 
			",post_type = '" + postType + '\'' + 
			",discussion_type_id = '" + discussionTypeId + '\'' + 
			",_id = '" + id + '\'' + 
			",post_privacy = '" + postPrivacy + '\'' + 
			",post_preview_code = '" + postPreviewCode + '\'' + 
			",post_preview_title = '" + postPreviewTitle + '\'' + 
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
		dest.writeByte(this.isLikeLocal ? (byte) 1 : (byte) 0);
		dest.writeByte(this.isFavLocal ? (byte) 1 : (byte) 0);
		dest.writeInt(this.postLikes);
		dest.writeString(this.postImage);
		dest.writeString(this.discussionType);
		dest.writeString(this.postPreviewImage);
		dest.writeInt(this.postComments);
		dest.writeInt(this.postShare);
		dest.writeString(this.postPreviewCodeUrl);
		dest.writeString(this.createdAt);
		dest.writeString(this.postPreviewDescription);
		dest.writeString(this.postViewType);
		dest.writeString(this.post);
		dest.writeString(this.updatedAt);
		dest.writeString(this.postPreviewUrl);
		dest.writeString(this.postPreviewType);
		dest.writeParcelable(this.postBy, flags);
		dest.writeString(this.postType);
		dest.writeString(this.discussionTypeId);
		dest.writeString(this.id);
		dest.writeString(this.postPrivacy);
		dest.writeString(this.postPreviewCode);
		dest.writeString(this.postPreviewTitle);
		dest.writeInt(this.commentLike);
		dest.writeInt(this.prayerAnswer);
		dest.writeString(this.prayerAnswerComment);
	}

	protected AllpostsItem(Parcel in) {
		this.isLikeLocal = in.readByte() != 0;
		this.isFavLocal = in.readByte() != 0;
		this.postLikes = in.readInt();
		this.postImage = in.readString();
		this.discussionType = in.readString();
		this.postPreviewImage = in.readString();
		this.postComments = in.readInt();
		this.postShare = in.readInt();
		this.postPreviewCodeUrl = in.readString();
		this.createdAt = in.readString();
		this.postPreviewDescription = in.readString();
		this.postViewType = in.readString();
		this.post = in.readString();
		this.updatedAt = in.readString();
		this.postPreviewUrl = in.readString();
		this.postPreviewType = in.readString();
		this.postBy = in.readParcelable(PostBy.class.getClassLoader());
		this.postType = in.readString();
		this.discussionTypeId = in.readString();
		this.id = in.readString();
		this.postPrivacy = in.readString();
		this.postPreviewCode = in.readString();
		this.postPreviewTitle = in.readString();
		this.commentLike = in.readInt();
		this.prayerAnswer = in.readInt();
		this.prayerAnswerComment = in.readString();
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