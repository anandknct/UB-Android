package com.unitybound.main.friendrequest.beans.RejectReq;

import com.google.gson.annotations.SerializedName;

public class RejectReqResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("status")
	private String status;

	@SerializedName("send_by_user_id")
	private String sendByUserId;

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

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setSendByUserId(String sendByUserId){
		this.sendByUserId = sendByUserId;
	}

	public String getSendByUserId(){
		return sendByUserId;
	}

	@Override
 	public String toString(){
		return 
			"RejectReqResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",status = '" + status + '\'' + 
			",send_by_user_id = '" + sendByUserId + '\'' + 
			"}";
		}
}