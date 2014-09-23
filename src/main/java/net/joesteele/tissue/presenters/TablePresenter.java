package net.joesteele.tissue.presenters;

/**
 * Created by joesteele on 9/6/14.
 */
public interface TablePresenter {
  public String[] headers();

  public String[][] rows();
}
