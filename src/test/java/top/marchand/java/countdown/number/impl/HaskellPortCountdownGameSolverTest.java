package top.marchand.java.countdown.number.impl;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import top.marchand.java.countdown.number.CountdownGameSolver;
import top.marchand.java.countdown.number.Draw;

import java.util.List;
import java.util.stream.Stream;

public class HaskellPortCountdownGameSolverTest {
  @Test
  @DisplayName("solving 75, 1, 6, 7, 8, 10 / 369 should give 8 + 7 - 10 * 75 - 6")
  public void test1() {
    // Given
    Draw draw = new Draw(new int[]{75, 1, 6, 7, 8, 10}, 369);
    CountdownGameSolver solver = new HaskellPortCountdownGameSolver();
    CountdownGameSolver.Solution expectedSolution = new CountdownGameSolver.Solution("8 + 7 - 10 * 75 - 6");
    // When
    List<CountdownGameSolver.Solution> solutions = solver.solve(draw);
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly
        .assertThat(solutions)
        .contains(expectedSolution);
    softly.assertThat(solutions)
          .hasSize(1);
    softly.assertAll();
  }

  @Test
  @DisplayName("subsets of (1,2,3) should return ((1),(2),(3),(1,2),(1,3),(2,3),(1,2,3))")
  public void test2() {
    // Given
    List<Integer> numbers = List.of(1, 2, 3);
    // When
    List<List<Integer>> actual = HaskellPortCountdownGameSolver.allSubsetsFrom(numbers);
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual)
          .hasSize(8);
    softly.assertThat(actual)
          .contains(List.of());
    softly.assertThat(actual)
          .contains(List.of(1));
    softly.assertThat(actual)
          .contains(List.of(2));
    softly.assertThat(actual)
          .contains(List.of(3));
    softly.assertThat(actual)
          .contains(List.of(1, 2));
    softly.assertThat(actual)
          .contains(List.of(1, 3));
    softly.assertThat(actual)
          .contains(List.of(2, 3));
    softly.assertThat(actual)
          .contains(List.of(1, 2, 3));
    softly.assertAll();
  }

  @Test
  @DisplayName("interleave of 1 element returns  the same element")
  public void test3() {
    // Given
    int x = 1;
    List<Integer> numbers = List.of();
    // When
    List<List<Integer>> actual = HaskellPortCountdownGameSolver
        .interleave(x, numbers)
        .toList();
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual)
          .hasSize(1);
    softly.assertThat(actual)
          .contains(List.of(1));
    softly.assertAll();
  }

  @Test
  @DisplayName("interleave of 1 element and a 1 size list returns both elements in each positions")
  public void test4() {
    // Given
    int x = 1;
    List<Integer> numbers = List.of(2);
    // When
    List<List<Integer>> actual = HaskellPortCountdownGameSolver
        .interleave(x, numbers)
        .toList();
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual)
          .hasSize(2);
    softly.assertThat(actual)
          .contains(List.of(1, 2));
    softly.assertThat(actual)
          .contains(List.of(2, 1));
    softly.assertAll();
  }

  @Test
  @DisplayName("interleave of 1 element and a 2 size list returns three elements in each positions")
  public void test5() {
    // Given
    int x = 1;
    List<Integer> numbers = List.of(2, 3);
    // When
    List<List<Integer>> actual = HaskellPortCountdownGameSolver
        .interleave(x, numbers)
        .toList();
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(actual)
          .hasSize(5);
    softly.assertThat(actual)
          .contains(List.of(1, 2, 3));
    softly.assertThat(actual)
          .contains(List.of(2, 1, 3));
    softly.assertThat(actual)
          .contains(List.of(2, 3, 1));
    softly.assertThat(actual)
          .contains(List.of(3, 1, 2));
    softly.assertThat(actual)
          .contains(List.of(3, 2, 1));
    softly.assertAll();
  }

}