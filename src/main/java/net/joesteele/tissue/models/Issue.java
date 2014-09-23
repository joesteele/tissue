package net.joesteele.tissue.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joesteele on 9/5/14.
 */
public class Issue {
  public String url;
  public String htmlUrl;
  public int number;
  public String state;
  public String title;
  public String body;
  public User user;
  public List<Label> labels = new ArrayList<>();
  public User assignee;
  public Milestone milestone;
  @SerializedName("comments")
  public int numComments;
  public List<Comment> issueComments = new ArrayList<>();
  public Pull pullRequest;
  public String closedAt;
  public String createdAt;
  public String updatedAt;
}
