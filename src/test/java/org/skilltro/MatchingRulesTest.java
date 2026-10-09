package org.skilltro;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MatchingRulesTest {
  @Test void calculatesRequiredSkillOverlap() {
    assertEquals(50, MatchingRules.requiredSkillOverlap(1, 2));
    assertEquals(67, MatchingRules.requiredSkillOverlap(2, 3));
  }

  @Test void handlesNoRequiredSkillsAndZeroOverlap() {
    assertEquals(100, MatchingRules.requiredSkillOverlap(0, 0));
    assertEquals(0, MatchingRules.requiredSkillOverlap(0, 3));
  }
}
