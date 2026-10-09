package org.skilltro;

public final class MatchingRules {
  private MatchingRules() {}

  public static int requiredSkillOverlap(int matchedRequired, int totalRequired) {
    if (totalRequired <= 0) return 100;
    return (int) Math.round(100.0 * matchedRequired / totalRequired);
  }
}
