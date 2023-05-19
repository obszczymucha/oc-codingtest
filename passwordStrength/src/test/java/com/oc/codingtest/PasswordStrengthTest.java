package com.oc.codingtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PasswordStrengthTest {

  private static final Logger log = LoggerFactory.getLogger(PasswordStrengthTest.class);

  PasswordStrength ps = new PasswordStrength();

  private static Stream<Arguments> maxRepetitionsArgProvider() {
    return Stream.of(
      Arguments.of("abcdefg", 0, false),

      Arguments.of("password", 0, false),
      Arguments.of("password", 1, false),
      Arguments.of("password", 2, true),

      Arguments.of("touchwood", 2, false),
      Arguments.of("touchwood", 3, true),

      Arguments.of("TheQuickBrownFoxJumpsOverTheLazyDog", 2, false),
      Arguments.of("TheQuickBrownFoxJumpsOverTheLazyDog", 3, true)
    );
  }

  @ParameterizedTest
  @DisplayName("Max character repetition tests")
  @MethodSource("com.oc.codingtest.PasswordStrengthTest#maxRepetitionsArgProvider")
  void checkingMaxRepetitionsOnly(String password, int maxRepetitions, boolean expectedResult) {
    assertEquals(expectedResult, ps.isPasswordPermissible(password, maxRepetitions, Integer.MAX_VALUE));
  }

  private static Stream<Arguments> maxSequenceLenArgProvider() {
    return Stream.of(
      //simple ascending
      Arguments.of("abcdef", 0, 6),
      Arguments.of("abcdef", 5, 6),
      Arguments.of("abcdef", 6, 6),
      //simple descending
      Arguments.of("fedcba", 0, 6),
      Arguments.of("fedcba", 5, 6),
      Arguments.of("fedcba", 6, 6),

      //numeric ascending
      Arguments.of("0123456789", 9, 10),
      Arguments.of("0123456789", 10, 10),

      //numeric descending
      Arguments.of("9876543210", 9, 10),
      Arguments.of("9876543210", 10, 10),

      //ascending - mixed case
      Arguments.of("ABCdef", 2, 6),
      Arguments.of("ABCdef", 5, 6),
      Arguments.of("ABCdef", 6, 6),

      //descending - mixed case
      Arguments.of("fedCBA", 2, 6),
      Arguments.of("fedCBA", 5, 6),
      Arguments.of("fedCBA", 6, 6),

      //Exclude/handle non-alphanumeric chars
      Arguments.of("/012345678", 8, 9),
      Arguments.of("/012345678", 9, 9),
      Arguments.of("0123456789:", 9, 10),
      Arguments.of("0123456789:", 10, 10)
    );
  }

  @ParameterizedTest
  @DisplayName("Max sequence length tests")
  @MethodSource("com.oc.codingtest.PasswordStrengthTest#maxSequenceLenArgProvider")
  void checkingMaxSequenceLenOnly(String password, int maxLen, int expectedResult) {
    assertEquals(expectedResult, ps.getMaxSequenceLen(password));
  }

}