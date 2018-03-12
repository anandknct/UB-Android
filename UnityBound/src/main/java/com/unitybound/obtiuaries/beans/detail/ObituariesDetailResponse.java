package com.unitybound.obtiuaries.beans.detail;

import com.google.gson.annotations.SerializedName;

public class ObituariesDetailResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("msg")
	private String msg;

	@SerializedName("obituary")
	private Obituary obituary;

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

	public void setObituary(Obituary obituary){
		this.obituary = obituary;
	}

	public Obituary getObituary(){
		return obituary;
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
			"ObituariesDetailResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",msg = '" + msg + '\'' + 
			",obituary = '" + obituary + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}