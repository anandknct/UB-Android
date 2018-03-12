package com.unitybound.groups.beans.deleteGroupMember;

import com.google.gson.annotations.SerializedName;

public class DeleteGroupMemberResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("success")
	private String success;

	@SerializedName("delete_user_id")
	private String deleteUserId;

	@SerializedName("id")
	private String id;

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

	@Override
 	public String toString(){
		return 
			"DeleteGroupMemberResponse{" + 
			"msg = '" + msg + '\'' + 
			",success = '" + success + '\'' + 
			",delete_user_id = '" + deleteUserId + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}