package org.cxsmxs.telegram.entities;

import com.google.gson.annotations.SerializedName;

public class Update {
    @SerializedName("update_id")
    private long updateId;
    private Message message;
}