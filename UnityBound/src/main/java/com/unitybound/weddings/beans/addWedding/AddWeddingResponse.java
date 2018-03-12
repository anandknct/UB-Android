package com.unitybound.weddings.beans.addWedding;

import com.google.gson.annotations.SerializedName;

public class AddWeddingResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("wedding_id")
	private String weddingId;

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

	public void setWeddingId(String weddingId){
		this.weddingId = weddingId;
	}

	public String getWeddingId(){
		return weddingId;
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
			"AddWeddingResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",wedding_id = '" + weddingId + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}