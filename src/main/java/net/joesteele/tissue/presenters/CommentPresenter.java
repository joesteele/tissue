package net.joesteele.tissue.presenters;

import net.joesteele.tissue.DateHelper;
import net.joesteele.tissue.models.Comment;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by joesteele on 9/5/14.
 */
public class CommentPresenter implements TableRowPresenter {
  public final Comment comment;

  private static final int MAX_COMMENT_LENGTH = 75;

  public CommentPresenter(final Comment comment) {
    this.comment = comment;
  }

  public String[] columns() {
    return new String[]{author() + "\n" + created(), body()};
  }

  public String author() {
    return comment.user.login;
  }

  public String body() {
    return WordUtils.wrap(comment.body.replaceAll("\\r", "").trim(), MAX_COMMENT_LENGTH, "\n", true);
  }

  public String created() {
    return DateHelper.getRelativeTimeSpanString(comment.createdAt);
  }
}
