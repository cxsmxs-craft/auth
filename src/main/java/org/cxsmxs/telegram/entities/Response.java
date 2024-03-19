package org.cxsmxs.telegram.entities;

public class Response<T> {
  private boolean ok;
  private T result;
  private String description;
}