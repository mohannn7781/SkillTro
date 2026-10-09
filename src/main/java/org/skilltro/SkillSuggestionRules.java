package org.skilltro;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class SkillSuggestionRules {
  private static final Map<String, List<String>> PHRASES = Map.of(
      "Carpentry", List.of("carpenter", "carpentry", "woodwork", "furniture"),
      "Welding", List.of("weld", "welder"),
      "Electrical wiring", List.of("electrician", "wiring", "electrical"),
      "Plumbing", List.of("plumber", "plumbing", "pipe fitting"),
      "Masonry", List.of("mason", "brickwork", "masonry"),
      "Painting", List.of("painter", "painting", "paint"),
      "Cooking", List.of("cook", "cooking", "chef"),
      "Driving", List.of("driver", "driving"),
      "Machine operation", List.of("machine operator", "machine operation"),
      "First aid", List.of("first aid", "cpr")
  );

  private SkillSuggestionRules() {}

  public static List<String> suggest(List<String> availableSkills, String description,
                                     Set<String> reviewedSkillNames) {
    String text = description == null ? "" : description.toLowerCase(Locale.ROOT);
    Set<String> reviewed = reviewedSkillNames.stream()
        .map(name -> name.toLowerCase(Locale.ROOT)).collect(java.util.stream.Collectors.toSet());

    return availableSkills.stream()
        .filter(name -> !reviewed.contains(name.toLowerCase(Locale.ROOT)))
        .filter(name -> PHRASES.getOrDefault(name, List.of(name.toLowerCase(Locale.ROOT)))
            .stream().anyMatch(text::contains))
        .toList();
  }
}
