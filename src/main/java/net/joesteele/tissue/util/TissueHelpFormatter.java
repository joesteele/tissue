package net.joesteele.tissue.util;

import joptsimple.HelpFormatter;
import joptsimple.OptionDescriptor;
import joptsimple.internal.Rows;
import joptsimple.internal.Strings;

import java.util.*;

import static joptsimple.internal.Classes.shortNameOf;
import static joptsimple.internal.Strings.*;

/**
 * Created by joesteele on 9/25/14.
 */
public class TissueHelpFormatter implements HelpFormatter {
  static final char HYPHEN_CHAR = '-';
  static final String HYPHEN = String.valueOf(HYPHEN_CHAR);
  static final String DOUBLE_HYPHEN = "--";

  private Rows commandRows;
  private final Rows optionRows = new Rows(360, 13);

  public String format(Map<String, ? extends OptionDescriptor> options) {
    Comparator<OptionDescriptor> comparator =
      (first, second) -> first.options().iterator().next().compareTo(second.options().iterator().next());

    Set<OptionDescriptor> sorted = new TreeSet<>(comparator);
    sorted.addAll(options.values());

    addRows(sorted);

    return formattedHelpOutput();
  }

  public void addCommand(String name, String description) {
    if (commandRows == null) {
      commandRows = new Rows(360, 4);
      commandRows.add("Command", "Description");
      commandRows.add("-------", "-----------");
    }

    commandRows.add(name, description);
  }

  private String formattedHelpOutput() {
    StringBuilder formatted = new StringBuilder();

    formatted.append(LINE_SEPARATOR);

    String commandsDisplay = commandRows.render();
    if (!Strings.isNullOrEmpty(commandsDisplay)) {
      formatted.append(commandsDisplay)
        .append(LINE_SEPARATOR)
        .append(LINE_SEPARATOR);
    }

    formatted.append(optionRows.render());

    formatted.append(LINE_SEPARATOR);

    return formatted.toString();
  }

  private void addRows(Collection<? extends OptionDescriptor> options) {
    if (options.isEmpty())
      optionRows.add("No options specified", "");
    else {
      addHeaders(options);
      addOptions(options);
    }

    optionRows.fitToWidth();
  }


  private void addHeaders(Collection<? extends OptionDescriptor> options) {
    if (hasRequiredOption(options)) {
      optionRows.add("Option (* = required)", "Description");
      optionRows.add("---------------------", "-----------");
    } else {
      optionRows.add("Option", "Description");
      optionRows.add("------", "-----------");
    }
  }

  private boolean hasRequiredOption(Collection<? extends OptionDescriptor> options) {
    for (OptionDescriptor each : options) {
      if (each.isRequired())
        return true;
    }

    return false;
  }

  private void addOptions(Collection<? extends OptionDescriptor> options) {
    for (OptionDescriptor each : options) {
      if (!each.representsNonOptions())
        optionRows.add(createOptionDisplay(each), createDescriptionDisplay(each));
    }
  }

  private String createOptionDisplay(OptionDescriptor descriptor) {
    StringBuilder buffer = new StringBuilder(descriptor.isRequired() ? "* " : "");

    for (Iterator<String> i = descriptor.options().iterator(); i.hasNext(); ) {
      String option = i.next();
      buffer.append(option.length() > 1 ? DOUBLE_HYPHEN : HYPHEN);
      buffer.append(option);

      if (i.hasNext())
        buffer.append(", ");
    }

    maybeAppendOptionInfo(buffer, descriptor);

    return buffer.toString();
  }

  private void maybeAppendOptionInfo(StringBuilder buffer, OptionDescriptor descriptor) {
    String indicator = extractTypeIndicator(descriptor);
    String description = descriptor.argumentDescription();
    if (indicator != null || !isNullOrEmpty(description))
      appendOptionHelp(buffer, indicator, description, descriptor.requiresArgument());
  }

  private String extractTypeIndicator(OptionDescriptor descriptor) {
    String indicator = descriptor.argumentTypeIndicator();

    if (!isNullOrEmpty(indicator) && !String.class.getName().equals(indicator))
      return shortNameOf(indicator);

    return null;
  }

  private void appendOptionHelp(StringBuilder buffer, String typeIndicator, String description, boolean required) {
    if (required)
      appendTypeIndicator(buffer, typeIndicator, description, '<', '>');
    else
      appendTypeIndicator(buffer, typeIndicator, description, '[', ']');
  }

  private void appendTypeIndicator(StringBuilder buffer, String typeIndicator, String description,
                                   char start, char end) {
    buffer.append(' ').append(start);
    if (typeIndicator != null)
      buffer.append(typeIndicator);

    if (!isNullOrEmpty(description)) {
      if (typeIndicator != null)
        buffer.append(": ");

      buffer.append(description);
    }

    buffer.append(end);
  }

  private String createDescriptionDisplay(OptionDescriptor descriptor) {
    List<?> defaultValues = descriptor.defaultValues();
    if (defaultValues.isEmpty())
      return descriptor.description();

    String defaultValuesDisplay = createDefaultValuesDisplay(defaultValues);
    return (descriptor.description() + ' ' + surround("default: " + defaultValuesDisplay, '(', ')')).trim();
  }

  private String createDefaultValuesDisplay(List<?> defaultValues) {
    return defaultValues.size() == 1 ? defaultValues.get(0).toString() : defaultValues.toString();
  }
}
