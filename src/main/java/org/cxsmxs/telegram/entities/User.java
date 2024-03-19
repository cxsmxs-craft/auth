package org.cxsmxs.telegram.entities;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private long id;

    @SerializedName("is_bot")
    private boolean isBot;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("username")
    private String username;

    @SerializedName("language_code")
    private String languageCode;
}