package net.joesteele.tissue;

import net.joesteele.tissue.models.Comment;
import net.joesteele.tissue.models.Issue;
import rx.Observable;

import java.util.List;
import java.util.Map;

/**
 * Created by joesteele on 9/5/14.
 */
public interface GitHub {
  public static Observable<List<Issue>> issues(String owner, String repo, Map<String, Object> params) {
    return Client.get().issues(owner, repo, params);
  }

  public static Observable<Issue> issue(String owner, String repo, int number) {
    Observable<Issue> issueReq = Client.get().issue(owner, repo, number);
    Observable<List<Comment>> commentsReq = Client.get().comments(owner, repo, number);

    return Observable.combineLatest(issueReq, commentsReq, (issue, comments) -> {
      issue.issueComments = comments;
      return issue;
    });
  }
}
