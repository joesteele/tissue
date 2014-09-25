package net.joesteele.tissue.util;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.joesteele.tissue.Tissue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * Created by joesteele on 9/21/14.
 */
public class OptionsHelper {
  private static OptionParser parser;

  static {
    parser = new OptionParser();

    parser.acceptsAll(asList("l", "label"), "scope issues to comma-separated labels").withRequiredArg().ofType(String.class); // labels
    parser.acceptsAll(asList("m", "milestone"), "usage: -m \"*<string>\" OR -m <number> - show issues for a milestone").withRequiredArg(); // milestone
    parser.acceptsAll(asList("u", "unscoped"), "unscoped; ignore project '.tissue' file"); // unscoped
    parser.acceptsAll(asList("a", "all"), "show all issues, both open and closed"); // all - open and closed
    parser.acceptsAll(asList("c", "closed"), "show only closed issues"); // closed issues
    parser.acceptsAll(asList("mine"), "show issues assigned to you - requires $TISSUE_USERNAME to be set"); // mine - assigned to me
    parser.accepts("limit", "limit returned results to <number>").withRequiredArg().ofType(Integer.class); // limit - limit returned results

    parser.accepts("help", "show this help info").forHelp(); // help

    TissueHelpFormatter helpFormatter = new TissueHelpFormatter();

    helpFormatter.addCommand("help", "show this help info");
    helpFormatter.addCommand("issue <number>, i <number>", "show the issue for the given <number>");
    helpFormatter.addCommand("open <number>, o <number>", "open the issue page for the given <number> in the default browser");
    helpFormatter.addCommand("new, n", "open the new issue page in the default browser");

    parser.formatHelpWith(helpFormatter);
  }

  public static OptionParser parser() {
    return parser;
  }

  public static OptionSet parse(String[] args) {
    return parser().parse(args);
  }

  public static Map<String, Object> paramsFrom(OptionSet options) {
    Map<String, Object> params = new HashMap<>();

    List<String> labels = localProjectLabels();

    if (options.has("u")) {
      labels.clear();
    }

    if (options.has("l") || options.has("label")) {
      Object value = options.has("l") ? options.valueOf("l") : options.valueOf("label");
      labels.addAll(asList(value.toString().split(",")));

      labels = labels.stream()
        .map(String::trim)
        .distinct()
        .collect(Collectors.toList());
    }

    if (labels.size() > 0) {
      params.put("labels", String.join(",", labels));
    }

    if (options.has("m") || options.has("milestone")) {
      Object value = options.has("m") ? options.valueOf("m") : options.valueOf("milestone");
      params.put("milestone", value);
    }

    if (options.has("a") || options.has("all")) {
      params.put("state", "all");
    } else if (options.has("c") || options.has("closed")) {
      params.put("state", "closed");
    }

    if (options.has("mine")) {
      if (Tissue.username == null) {
        throw new IllegalArgumentException("You must define ENV['TISSUE_USERNAME'] in order to use the 'mine' option");
      }

      params.put("assignee", Tissue.username);
    }

    return params;
  }

  private static List<String> localProjectLabels() {
    List<String> labels = new ArrayList<>();

    try {
      File projectScoping = new File(System.getProperty("user.dir") + "/" + ".tissue");
      if (projectScoping.exists() && !projectScoping.isDirectory()) {
        labels.addAll(asList(new String(Files.readAllBytes(Paths.get(".tissue"))).split(",")));
      }
    } catch (IOException e) {
      System.out.println("Error reading project labels - leaving unscoped.");
    }

    return labels;
  }
}
