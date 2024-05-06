package com.oc.codingtest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import lombok.val;

public class PasswordStrength {
  public boolean isPasswordPermissible(final String password, final int maxAllowedRepetitionCount, final int maxAllowedSequenceLength) {
    // This method accepts a password (String) and calculates two password strength parameters:
    // Repetition count and Max Sequence length
    return getMaxRepetitionCount(password) <= maxAllowedRepetitionCount
             && getMaxSequenceLen(password) <= maxAllowedSequenceLength;
  }

  /**
   * Repetition count - the number of occurrences of the *most repeated* character within the password
   *   eg1: "Melbourne" has a repetition count of 2 - for the 2 non-consecutive "e" characters.
   *   eg2: "passwords" has a repetition count of 3 - for the 3 "s" characters
   *   eg3: "lucky" has a repetition count of 1 - each character appears only once.
   *   eg4: "Elephant" has a repetition count of 1 - as the two "e" characters have different cases (ie one "E", one "e")
   * The repetition count should be case-sensitive.
   * @param password
   * @return
   */
  public int getMaxRepetitionCount(final String password) {
    if (password.isEmpty())
      return 0;

    val map = new HashMap<Integer, Integer>();
    password.chars().forEach((c) -> map.put(c, map.getOrDefault(c, 0) + 1));

    return Collections.max(map.values());
  }

  /**
   * Max Sequence length - The length of the longest ascending/descending sequence of alphabetical or numeric characters
   * eg: "4678" and "4321" would both have sequence length of 4
   * eg2: "cdefgh" would have a sequence length of 6
   * eg3: "password123" would have a max. sequence length of 3 - for the sequence of "123".
   * eg3a: "1pass2word3" would have a max. sequence length of 0 - as there is no sequence.
   * eg3b: "passwordABC" would have a max. sequence length of 3 - for the sequence of "ABC".
   * eg4: "AbCdEf" would have a sequence length of 6, even though it is mixed case.
   * eg5: "ABC_DEF" would have a sequence length of 3, because the special character breaks the progression
   * Check the supplied password.  Return true if the repetition count and sequence length are below or equal to the
   * specified maximum.  Otherwise, return false.
   * @param password
   * @return
   */
  public int getMaxSequenceLen(final String password) {
    var result = 0;
    var sequenceLen = 0;
    var lastDirection = SequenceDirection.NONE;
    var lastChar = Optional.<Character>empty();

    for (char c : password.toLowerCase().toCharArray()) {
      val isAlphanumeric = Character.isLetterOrDigit(c);

      if (isAlphanumeric && lastChar.isEmpty()) {
        lastChar = Optional.of(c);
        continue;
      }

      if (lastChar.isEmpty())
        continue;

      val direction = getDirection(isAlphanumeric, c, lastChar.get());
      sequenceLen = direction == lastDirection ? sequenceLen + 1 : 2;

      if (sequenceLen > result)
        result = sequenceLen;

      lastDirection = direction;
      lastChar = Optional.of(c);
    }

    return result;
  }

  private enum SequenceDirection {
    ASCENDING, DESCENDING, NONE
  }

  private SequenceDirection getDirection(final boolean isAlphanumeric, final char c, final char lastChar) {
    if (!isAlphanumeric || c == lastChar)
      return SequenceDirection.NONE;

    return lastChar + 1 == c ? SequenceDirection.ASCENDING : SequenceDirection.DESCENDING;
  }
}
