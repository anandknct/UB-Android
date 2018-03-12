package com.unitybound.church.beans.joinRequest;

import com.google.gson.annotations.SerializedName;

public class JoinChurchResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("success")
	private String success;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	@Override
 	public String toString(){
		return 
			"JoinChurchResponse{" + 
			"msg = '" + msg + '\'' + 
			",success = '" + success + '\'' + 
			"}";
		}
}