package com.unitybound.church.listners;

/**
 * Created by Admin on 10/22/2017.
 */

public interface MembershipRequestListner {
    void onAcceptReq(String id,int position);
    void onRejectReq(String id,int position);
}
