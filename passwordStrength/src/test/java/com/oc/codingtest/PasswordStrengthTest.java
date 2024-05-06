package com.oc.codingtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PasswordStrengthTest {

  PasswordStrength ps = new PasswordStrength();

  @SuppressWarnings("unused")
  private static Stream<Arguments> maxRepetitionsArgProvider() {
    return Stream.of(
      Arguments.of("abcdefg", 1),
      Arguments.of("password", 2),
      Arguments.of("touchwood", 3),
      // The check should be case-sensitive
      Arguments.of("TheQuickBrownFoxJumpsOverTheLazyDog", 3),

      // I didn't add a test that checks for null, because I trust that such
      // validation is handled elsewhere. Perhaps responding with BadRequest
      // and not reaching the service layer at all?
      Arguments.of("", 0),
      Arguments.of(" ", 1),
      Arguments.of(":", 1),
      Arguments.of("a", 1)
    );
  }

  @ParameterizedTest
  @DisplayName("Max character repetition tests")
  @MethodSource("com.oc.codingtest.PasswordStrengthTest#maxRepetitionsArgProvider")
  void checkingMaxRepetitionsOnly(String password, int expectedResult) {
    assertEquals(expectedResult, ps.getMaxRepetitionCount(password));
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> maxSequenceLenArgProvider() {
    return Stream.of(
      //simple ascending
      Arguments.of("abcdef", 6),
      //simple descending
      Arguments.of("fedcba", 6),
      //numeric ascending
      Arguments.of("0123456789", 10),
      //numeric descending
      Arguments.of("9876543210", 10),
      //ascending - mixed case
      Arguments.of("ABCdef", 6),
      //descending - mixed case
      Arguments.of("fedCBA", 6),

      //Exclude/handle non-alphanumeric chars
      Arguments.of("/012345678", 9),
      Arguments.of("0123456789:", 10),
      Arguments.of("01234*567:", 5),

      //Ascending and descending mixed
      Arguments.of("23454321", 5),

      // Not adding null test on purpose here either.
      Arguments.of("", 0),
      Arguments.of(" ", 0),
      Arguments.of("a", 0),
      Arguments.of(":", 0),
      Arguments.of("ab", 2)
    );
  }

  @ParameterizedTest
  @DisplayName("Max sequence length tests")
  @MethodSource("com.oc.codingtest.PasswordStrengthTest#maxSequenceLenArgProvider")
  void checkingMaxSequenceLenOnly(String password, int expectedResult) {
    assertEquals(expectedResult, ps.getMaxSequenceLen(password));
  }

}
