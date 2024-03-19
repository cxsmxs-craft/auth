package org.cxsmxs.telegram.entities;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("message_id")
    private long messageId;

    private User from;
    @SerializedName("sender_chat")
    private Chat senderChat;
    private long date;
    private Chat chat;
    @SerializedName("reply_to_message")
    private Message replyToMessage;
    private String text;
}