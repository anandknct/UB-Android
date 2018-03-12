package com.unitybound.main.home.fragment.beans.like_comment;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("commentlike")
	private int commentlike;

	@SerializedName("isadd")
	private int isadd;

	public void setCommentlike(int commentlike){
		this.commentlike = commentlike;
	}

	public int getCommentlike(){
		return commentlike;
	}

	public void setIsadd(int isadd){
		this.isadd = isadd;
	}

	public int getIsadd(){
		return isadd;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"commentlike = '" + commentlike + '\'' + 
			",isadd = '" + isadd + '\'' + 
			"}";
		}
}