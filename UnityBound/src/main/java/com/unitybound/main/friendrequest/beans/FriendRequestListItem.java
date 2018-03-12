package com.unitybound.main.friendrequest.beans;

import com.google.gson.annotations.SerializedName;
import com.unitybound.notification.beans.SendBy;

public class FriendRequestListItem{

	@SerializedName("send_to")
	private String sendTo;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	@SerializedName("send_by")
	private SendBy sendBy;

	@SerializedName("is_accept")
	private int isAccept;

	public void setSendTo(String sendTo){
		this.sendTo = sendTo;
	}

	public String getSendTo(){
		return sendTo;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSendBy(SendBy sendBy){
		this.sendBy = sendBy;
	}

	public SendBy getSendBy(){
		return sendBy;
	}

	public void setIsAccept(int isAccept){
		this.isAccept = isAccept;
	}

	public int getIsAccept(){
		return isAccept;
	}

	@Override
 	public String toString(){
		return 
			"FriendRequestListItem{" + 
			"send_to = '" + sendTo + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",_id = '" + id + '\'' + 
			",send_by = '" + sendBy + '\'' + 
			",is_accept = '" + isAccept + '\'' + 
			"}";
		}
}