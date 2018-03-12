package com.unitybound.church.beans.blockChurchMember;

import com.google.gson.annotations.SerializedName;

public class UnBlockChurchMemberResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("success")
	private String success;

	@SerializedName("unblock_user_id")
	private String unblockUserId;

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

	public void setUnblockUserId(String unblockUserId){
		this.unblockUserId = unblockUserId;
	}

	public String getUnblockUserId(){
		return unblockUserId;
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
			"UnBlockChurchMemberResponse{" + 
			"msg = '" + msg + '\'' + 
			",success = '" + success + '\'' + 
			",unblock_user_id = '" + unblockUserId + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}