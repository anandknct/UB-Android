package com.unitybound.church.beans.deleteMember;

import com.google.gson.annotations.SerializedName;

public class DeleteChurchMemberResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("delete_user_id")
	private String deleteUserId;

	@SerializedName("id")
	private String id;

	@SerializedName("status")
	private String status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setDeleteUserId(String deleteUserId){
		this.deleteUserId = deleteUserId;
	}

	public String getDeleteUserId(){
		return deleteUserId;
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
			"DeleteChurchMemberResponse{" + 
			"msg = '" + msg + '\'' + 
			",delete_user_id = '" + deleteUserId + '\'' + 
			",id = '" + id + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}