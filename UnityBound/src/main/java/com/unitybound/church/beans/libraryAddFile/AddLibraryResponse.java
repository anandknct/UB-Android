package com.unitybound.church.beans.libraryAddFile;

import com.google.gson.annotations.SerializedName;

public class AddLibraryResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("status")
	private String status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setStatuscode(String statuscode){
		this.statuscode = statuscode;
	}

	public String getStatuscode(){
		return statuscode;
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
			"AddLibraryResponse{" + 
			"msg = '" + msg + '\'' + 
			",statuscode = '" + statuscode + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}