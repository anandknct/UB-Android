package com.unitybound.account.beans.timeline;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PostComments{

	@SerializedName("Comments")
	private Comments comments;

	@SerializedName("Reply")
	private List<Object> reply;

	public void setComments(Comments comments){
		this.comments = comments;
	}

	public Comments getComments(){
		return comments;
	}

	public void setReply(List<Object> reply){
		this.reply = reply;
	}

	public List<Object> getReply(){
		return reply;
	}

	@Override
 	public String toString(){
		return 
			"PostComments{" + 
			"comments = '" + comments + '\'' + 
			",reply = '" + reply + '\'' + 
			"}";
		}
}