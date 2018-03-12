package com.unitybound.main.my.prayer.request.model.answerPrayer;

import com.google.gson.annotations.SerializedName;

public class AnswerPrayerResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("post_id")
	private String postId;

	@SerializedName("success")
	private String success;

	@SerializedName("comment")
	private String comment;

	public void setStatuscode(String statuscode){
		this.statuscode = statuscode;
	}

	public String getStatuscode(){
		return statuscode;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setPostId(String postId){
		this.postId = postId;
	}

	public String getPostId(){
		return postId;
	}

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	@Override
 	public String toString(){
		return 
			"AnswerPrayerResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",post_id = '" + postId + '\'' + 
			",success = '" + success + '\'' + 
			",comment = '" + comment + '\'' + 
			"}";
		}
}