package net.joesteele.tissue;

import com.jakewharton.fliptables.FlipTable;
import joptsimple.OptionSet;
import net.joesteele.tissue.models.Issue;
import net.joesteele.tissue.presenters.IssuePresenter;
import net.joesteele.tissue.presenters.IssuesPresenter;
import net.joesteele.tissue.presenters.TablePresenter;
import rx.Observable;

import java.io.IOException;
import java.util.List;

/**
 * Created by joesteele on 9/5/14.
 */
public class Tissue {
  public static String token = System.getenv("TISSUE_TOKEN");
  public static String username = System.getenv("TISSUE_USERNAME");
  public static String repository = System.getenv("TISSUE_REPOSITORY");
  public static String owner;
  public static String repo;

  public static void main(String[] args) throws Exception {
    if (token == null) {
      throw new IllegalStateException("GitHub token missing. Did you set ENV['TISSUE_TOKEN']?");
    }

    if (repository == null) {
      throw new IllegalStateException("GitHub repository missing. Did you set ENV['TISSUE_REPOSITORY']?\n- Expected format: <owner/repo>");
    }

    String[] ownerRepo = repository.split("/");

    if (ownerRepo.length != 2) {
      throw new IllegalStateException("ENV['TISSUE_REPOSITORY'] Expected format: <owner/repo>");
    }

    owner = ownerRepo[0];
    repo = ownerRepo[1];

    boolean hasArgs = args.length > 0;
    if (hasArgs && (args[0].equals("new") || args[0].equals("n"))) {
      openNewIssue();
    } else if (hasArgs && (args[0].equals("open") || args[0].equals("o"))) {
      if (args.length < 2) {
        throw new IllegalArgumentException("usage: tissue open <number> OR tissue o <number>");
      }

      openIssue(args[1]);
    } else if (hasArgs && (args[0].equals("issue") || args[0].equals("i"))) {
      if (args.length < 2) {
        throw new IllegalArgumentException("usage: tissue issue <number> OR tissue i <number>");
      }

      showIssue(args[1]);
    } else {
      showIssues(args);
    }
  }

  private static Observable<List<Issue>> issuesWithOptions(String[] args) {
    OptionSet options = OptionsHelper.parse(args);
    Observable<List<Issue>> issues = GitHub.issues(owner, repo, OptionsHelper.paramsFrom(options));

    if (options.has("limit")) {
      return issues
        .flatMap(Observable::from)
        .take((int) options.valueOf("limit"))
        .toList();
    }

    return issues;
  }

  private static void openNewIssue() throws IOException {
    String url = String.format("open https://github.com/%s/%s/issues/new", owner, repo);
    Runtime.getRuntime().exec(url);
    exit();
  }

  private static void openIssue(String number) throws IOException {
    String url = String.format("open https://github.com/%s/%s/issues/%s", owner, repo, number);
    Runtime.getRuntime().exec(url);
    exit();
  }

  private static void showIssues(String[] args) {
    issuesWithOptions(args)
      .map(IssuesPresenter::new)
      .finallyDo(Tissue::exit)
      .subscribe(Tissue::printTable);
  }

  private static void showIssue(String number) {
    GitHub.issue(owner, repo, Integer.valueOf(number))
      .map(IssuePresenter::new)
      .finallyDo(Tissue::exit)
      .subscribe(Tissue::printTable);
  }

  private static void printTable(TablePresenter table) {
    System.out.println(FlipTable.of(table.headers(), table.rows()));
  }

  private static void exit() {
    System.exit(0);
  }
}
