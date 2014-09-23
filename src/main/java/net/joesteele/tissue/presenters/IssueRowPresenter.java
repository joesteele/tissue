package net.joesteele.tissue.presenters;

import net.joesteele.tissue.DateHelper;
import net.joesteele.tissue.models.Issue;

/**
 * Created by joesteele on 9/5/14.
 */
public class IssueRowPresenter implements TableRowPresenter {
  private final Issue issue;

  private static final int MAX_TITLE_LENGTH = 50;

  public IssueRowPresenter(final Issue issue) {
    this.issue = issue;
  }

  public String[] columns() {
    return new String[]{number(), title() + "\n" + meta(), labels()};
  }

  public String number() {
    return String.valueOf(issue.number);
  }

  public String title() {
    String title = issue.title.trim();

    if (title.length() > MAX_TITLE_LENGTH) {
      return title.substring(0, MAX_TITLE_LENGTH).trim() + "...";
    } else {
      return title;
    }
  }

  public String meta() {
    return "opened " + created() + " by " + creator() + comments();
  }

  public String labels() {
    return String.join(", ", issue.labels.stream()
      .map(label -> label.name)
      .toArray(String[]::new));
  }

  public String comments() {
    switch (issue.numComments) {
      case 0:
        return "";
      case 1:
        return " (1 comment)";
      default:
        return " (" + issue.numComments + " comments)";
    }
  }

  public String created() {
    return DateHelper.getRelativeTimeSpanString(issue.createdAt);
  }

  public String creator() {
    return issue.user.login;
  }
}
