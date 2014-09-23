package net.joesteele.tissue.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by joesteele on 9/5/14.
 */
public class Milestone {
  public String url;
  public int number;
  public String state;
  public String title;
  public String description;
  public User creator;
  @SerializedName("open_issues")
  public int numOpenIssues;
  @SerializedName("closed_issues")
  public int numClosedIssues;
  public String createdAt;
  public String updatedAt;
  public String dueOn;
}
