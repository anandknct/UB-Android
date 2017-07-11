package com.unitybound.notification.model;

/**
 * Created by ABC on 7/4/17.
 */

public class NotificationData {

    public String number;

    public String callOption;

    public NotificationData(String callOption, String number) {
        this.callOption = callOption;
        this.number = number;
    }


    @Override
    public String toString() {
        return callOption + ":" + number;
    }
}
