package net.joesteele.tissue;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by joesteele on 9/21/14.
 */
public class OptionsHelper {
  public static OptionSet parse(String[] args) {
    OptionParser parser = new OptionParser();

    parser.accepts("l").withRequiredArg(); // labels
    parser.accepts("label").withRequiredArg(); // labels
    parser.accepts("m").withRequiredArg(); // milestone
    parser.accepts("milestone").withRequiredArg(); // milestone
    parser.accepts("u"); // unscoped
    parser.accepts("unscoped"); // unscoped
    parser.accepts("a"); // all - open and closed
    parser.accepts("all"); // all - open and closed
    parser.accepts("c"); // closed issues
    parser.accepts("closed"); // closed issues
    parser.accepts("mine"); // mine - assigned to me
    parser.accepts("limit").withRequiredArg().ofType(Integer.class); // limit - limit returned results

    return parser.parse(args);
  }

  public static Map<String, Object> paramsFrom(OptionSet options) {
    Map<String, Object> params = new HashMap<>();

    List<String> labels = localProjectLabels();

    if (options.has("u")) {
      labels.clear();
    }

    if (options.has("l") || options.has("label")) {
      Object value = options.has("l") ? options.valueOf("l") : options.valueOf("label");
      labels.addAll(Arrays.asList(value.toString().split(",")));

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
        labels.addAll(Arrays.asList(new String(Files.readAllBytes(Paths.get(".tissue"))).split(",")));
      }
    } catch (IOException e) {
      System.out.println("Error reading project labels - leaving unscoped.");
    }

    return labels;
  }
}
