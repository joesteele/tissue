package net.joesteele.tissue.presenters;

import net.joesteele.tissue.models.Issue;

import java.util.List;

/**
 * Created by joesteele on 9/5/14.
 */
public class IssuesPresenter implements TablePresenter {
  private final List<Issue> issues;

  public IssuesPresenter(final List<Issue> issues) {
    this.issues = issues;
  }

  public String[] headers() {
    return new String[]{"#", "Title", "Labels"};
  }

  public String[][] rows() {
    return issues.stream()
      .map(IssueRowPresenter::new)
      .map(IssueRowPresenter::columns)
      .toArray(String[][]::new);
  }
}
