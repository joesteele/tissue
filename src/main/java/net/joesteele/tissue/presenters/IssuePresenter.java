package net.joesteele.tissue.presenters;

import com.jakewharton.fliptables.FlipTable;
import net.joesteele.tissue.DateHelper;
import net.joesteele.tissue.models.Issue;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by joesteele on 9/5/14.
 */
public class IssuePresenter implements TablePresenter {
  private final Issue issue;

  private static final int MAX_DESC_LENGTH = 75;

  public IssuePresenter(final Issue issue) {
    this.issue = issue;
  }

  public String[] headers() {
    return new String[]{number() + " - " + title() + "\n" + meta() + "\n" + labels()};
  }

  public String[][] rows() {
    return new String[][]{{body()}, {"Comments:\n" + comments()}};
  }

  public String number() {
    return "#" + issue.number;
  }

  public String title() {
    return issue.title.trim();
  }

  public String body() {
    return WordUtils.wrap(issue.body.replaceAll("\\r", "").trim(), MAX_DESC_LENGTH, "\n", true);
  }

  public String creator() {
    return issue.user.login;
  }

  public String meta() {
    return issue.state + " - " + creator() + " opened this " + created() + assigned();
  }

  public String labels() {
    return "labels: " + String.join(", ", issue.labels.stream()
      .map(label -> label.name)
      .toArray(String[]::new));
  }

  public String assigned() {
    if (issue.assignee != null) {
      return " - assigned to " + issue.assignee.login;
    }
    return "";
  }

  public String created() {
    return DateHelper.getRelativeTimeSpanString(issue.createdAt);
  }

  public String comments() {
    CommentsPresenter table = new CommentsPresenter(issue.issueComments);
    return FlipTable.of(table.headers(), table.rows());
  }
}
