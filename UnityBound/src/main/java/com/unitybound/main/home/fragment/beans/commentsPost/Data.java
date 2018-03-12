package com.unitybound.main.home.fragment.beans.commentsPost;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("comments")
	private List<CommentsItem> comments;

	@SerializedName("islike")
	private Islike islike;

	@SerializedName("countLike")
	private int countLike;

	public void setComments(List<CommentsItem> comments){
		this.comments = comments;
	}

	public List<CommentsItem> getComments(){
		return comments;
	}

	public void setIslike(Islike islike){
		this.islike = islike;
	}

	public Islike getIslike(){
		return islike;
	}

	public void setCountLike(int countLike){
		this.countLike = countLike;
	}

	public int getCountLike(){
		return countLike;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"comments = '" + comments + '\'' + 
			",islike = '" + islike + '\'' + 
			",countLike = '" + countLike + '\'' + 
			"}";
		}
}