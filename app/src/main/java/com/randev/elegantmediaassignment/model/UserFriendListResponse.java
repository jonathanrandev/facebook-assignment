package com.randev.elegantmediaassignment.model;

import java.util.List;

/**
 * Created by jse on 2/8/2017.
 */

public class UserFriendListResponse {
    private List<Friend> data;

    public List<Friend> getData() {
        return data;
    }

    public void setData(List<Friend> data) {
        this.data = data;
    }
}
