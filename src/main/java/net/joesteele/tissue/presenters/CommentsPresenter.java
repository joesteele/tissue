package net.joesteele.tissue.presenters;

import net.joesteele.tissue.models.Comment;

import java.util.List;

/**
 * Created by joesteele on 9/5/14.
 */
public class CommentsPresenter implements TablePresenter {
  public final List<Comment> comments;

  public CommentsPresenter(final List<Comment> comments) {
    this.comments = comments;
  }

  public String[] headers() {
    return new String[]{"Author", "Comment"};
  }

  public String[][] rows() {
    return comments.stream()
      .map(CommentPresenter::new)
      .map(CommentPresenter::columns)
      .toArray(String[][]::new);
  }
}
