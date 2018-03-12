package com.unitybound.groups.beans.groupComments;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupCommentsResponse{

	@SerializedName("statuscode")
	private String statuscode;

	@SerializedName("memberStatus")
	private String memberStatus;

	@SerializedName("pageName")
	private String pageName;

	@SerializedName("allposts")
	private List<AllpostsItem> allposts;

	@SerializedName("status")
	private String status;

	@SerializedName("group")
	private Group group;

	public void setStatuscode(String statuscode){
		this.statuscode = statuscode;
	}

	public String getStatuscode(){
		return statuscode;
	}

	public void setMemberStatus(String memberStatus){
		this.memberStatus = memberStatus;
	}

	public String getMemberStatus(){
		return memberStatus;
	}

	public void setPageName(String pageName){
		this.pageName = pageName;
	}

	public String getPageName(){
		return pageName;
	}

	public void setAllposts(List<AllpostsItem> allposts){
		this.allposts = allposts;
	}

	public List<AllpostsItem> getAllposts(){
		return allposts;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setGroup(Group group){
		this.group = group;
	}

	public Group getGroup(){
		return group;
	}

	@Override
 	public String toString(){
		return 
			"GroupCommentsResponse{" + 
			"statuscode = '" + statuscode + '\'' + 
			",memberStatus = '" + memberStatus + '\'' + 
			",pageName = '" + pageName + '\'' + 
			",allposts = '" + allposts + '\'' + 
			",status = '" + status + '\'' + 
			",group = '" + group + '\'' + 
			"}";
		}
}