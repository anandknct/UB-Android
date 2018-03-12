package com.unitybound.main.friendrequest.listner;

/**
 * Created by Admin on 10/22/2017.
 */

public interface IfriendRequestListner {

    void onAcceptReq(String id,int position);
    void onRejectReq(String id,int position);
}
