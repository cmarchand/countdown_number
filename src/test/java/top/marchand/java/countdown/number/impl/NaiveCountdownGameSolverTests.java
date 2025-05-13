package top.marchand.java.countdown.number.impl;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import top.marchand.java.countdown.number.CountdownGameSolver;
import top.marchand.java.countdown.number.Draw;

import java.util.List;
import java.util.Set;

public class NaiveCountdownGameSolverTests {
  @Test
  @DisplayName("solving 75, 1, 6, 7, 8, 10 / 369 should give 8 + 7 - 10 * 75 - 6")
  public void test1() {
    // Given
    Draw draw = new Draw(new int[]{75, 1, 6, 7, 8, 10}, 369);
    CountdownGameSolver solver = new NaiveCountdownGameSolver();
    CountdownGameSolver.Solution expectedSolution = new CountdownGameSolver.Solution("8 + 7 - 10 * 75 - 6");
    // When
    List<CountdownGameSolver.Solution> solutions = solver.solve(draw);
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly
        .assertThat(solutions)
        .contains(expectedSolution);
    softly.assertThat(solutions).hasSize(1);
    softly.assertAll();
  }

  @Test
  @DisplayName("I should get 1024 different permutations of operators")
  public void test() {
    // When
    Set<List<NaiveCountdownGameSolver.Operator>> actual = new NaiveCountdownGameSolver().buildOperatorsPermutations();
    // Then
    Assertions.assertThat(actual).hasSize(1024);
  }
}
