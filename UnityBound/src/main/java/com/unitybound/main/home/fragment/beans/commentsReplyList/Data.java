package com.unitybound.main.home.fragment.beans.commentsReplyList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("comments")
	private List<CommentsItem> comments;

	public void setComments(List<CommentsItem> comments){
		this.comments = comments;
	}

	public List<CommentsItem> getComments(){
		return comments;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"comments = '" + comments + '\'' + 
			"}";
		}
}