package org.cxsmxs.telegram.entities;

import com.google.gson.annotations.SerializedName;

public class Chat {
  private long id;
  private String type;
  private String title;
  private String username;

  @SerializedName("first_name")
  private String firstName;

  @SerializedName("last_name")
  private String lastName;
}