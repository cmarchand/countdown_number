package top.marchand.java.countdown.number.impl;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PermuterTests {
  @Test
  @DisplayName("Permutting an array of length 1 should return only one permutation")
  public void test1() {
    // Given
    int[] source = {1};
    // When
    List<List<Integer>> actual = Permuter.permute(source);
    // Then
    Assertions.assertThat(actual).hasSize(1);
  }

  @Test
  @DisplayName("Permutting an array of length 2 should return 2 permutations")
  public void test2() {
    // Given
    int[] source = {1, 2};
    // When
    List<List<Integer>> actual = Permuter.permute(source);
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual).hasSize(2);
    softly.assertThat(actual.get(0)).isEqualTo(List.of(1, 2));
    softly.assertThat(actual.get(1)).isEqualTo(List.of(2, 1));
  }

  @Test
  @DisplayName("Permutting an array of length 3 should return n permutations")
  public void test3() {
    // Given
    int[] source = {1, 2, 3};
    // When
    List<List<Integer>> actual = Permuter.permute(source);
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual).hasSize(6);
    softly.assertThat(actual).contains(List.of(1, 2, 3));
    softly.assertThat(actual).contains(List.of(1, 3, 2));
    softly.assertThat(actual).contains(List.of(2, 1, 3));
    softly.assertThat(actual).contains(List.of(2, 3, 1));
    softly.assertThat(actual).contains(List.of(3, 1, 2));
    softly.assertThat(actual).contains(List.of(3, 2, 1));
  }

}