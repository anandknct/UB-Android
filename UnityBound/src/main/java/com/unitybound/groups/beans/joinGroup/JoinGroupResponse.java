package com.unitybound.groups.beans.joinGroup;

import com.google.gson.annotations.SerializedName;

public class JoinGroupResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("success")
	private String success;

	@SerializedName("memberCount")
	private int memberCount;

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

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setMemberCount(int memberCount){
		this.memberCount = memberCount;
	}

	public int getMemberCount(){
		return memberCount;
	}

	@Override
 	public String toString(){
		return 
			"JoinGroupResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",success = '" + success + '\'' + 
			",memberCount = '" + memberCount + '\'' + 
			"}";
		}
}