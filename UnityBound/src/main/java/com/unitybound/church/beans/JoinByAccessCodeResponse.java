package com.unitybound.church.beans;

import com.google.gson.annotations.SerializedName;

public class JoinByAccessCodeResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("memberCount")
	private int memberCount;

	@SerializedName("id")
	private String id;

	@SerializedName("status")
	private String status;

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

	public void setMemberCount(int memberCount){
		this.memberCount = memberCount;
	}

	public int getMemberCount(){
		return memberCount;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"JoinByAccessCodeResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",memberCount = '" + memberCount + '\'' + 
			",id = '" + id + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}