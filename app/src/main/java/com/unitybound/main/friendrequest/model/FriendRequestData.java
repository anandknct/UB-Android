package com.unitybound.main.friendrequest.model;

/**
 * Created by ABC on 7/4/17.
 */

public class FriendRequestData {

    public String number;

    public String callOption;

    public FriendRequestData(String callOption, String number) {
        this.callOption = callOption;
        this.number = number;
    }


    @Override
    public String toString() {
        return callOption + ":" + number;
    }
}
