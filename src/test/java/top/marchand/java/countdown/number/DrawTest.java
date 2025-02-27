package top.marchand.java.countdown.number;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class DrawTest {

  @Test
  @DisplayName("Builder.build should returns a Draw")
  public void test1() {
    // Given
    Draw.Builder builder = Draw.builder();
    // When
    Draw actual = builder.build();
    // Then
    Assertions.assertThat(actual)
              .isNotNull();
    Assertions.assertThat(actual.numbers())
              .hasSize(6);
  }

  @Test
  @DisplayName("No more than 4 bigs can be added")
  public void test2() {
    // Given
    Draw.Builder builder = Draw.builder();
    builder.big()
           .big()
           .big()
           .big();
    // Then
    Assertions
        .assertThatThrownBy(builder::big)
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("After 3 calls to small, there should be at least 3 smalls.")
  public void test3() {
    // Given
    Draw.Builder builder = Draw.builder();
    builder.small()
           .small()
           .small();
    Draw draw = builder.build();
    // When
    long actual = Arrays.stream(draw.numbers())
                        .filter(value -> value <= 10)
                        .count();
    // Then
    Assertions.assertThat(actual)
              .isGreaterThanOrEqualTo(3);
  }

  @Test
  @DisplayName("Adding a seventh entry should throws exception")
  public void test4() {
    // Given
    Draw.Builder builder = Draw.builder();
    builder.small()
           .big()
           .small()
           .big()
           .small()
           .big();
    // Then
    Assertions
        .assertThatThrownBy(builder::big)
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("A last one, for code cevrage and all branches of all if")
  public void test5() {
    // Given
    Draw.Builder builder = Draw.builder()
                             .big()
                             .big()
                             .small()
                             .small()
                             .small()
                             .small();
    // When
    Draw draw = builder.build();
    // Then
    Assertions
        .assertThat(draw)
        .isNotNull();
  }

  @Test
  @DisplayName("Constructing a Draw with 3 numbers should throw exception")
  public void test6() {
    // Given
    int[] numbers = {50, 1, 3};
    // Then
    Assertions
        .assertThatThrownBy(() -> new Draw(numbers, 777))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("Constructing a Draw with 99 as target should throw exception")
  public void test7() {
    // Given
    int[] numbers = {50, 1, 3, 25, 9, 8};
    // Then
    Assertions
        .assertThatThrownBy(() -> new Draw(numbers, 99))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("Constructing a Draw with 1000 as target should throw exception")
  public void test8() {
    // Given
    int[] numbers = {50, 1, 3, 25, 9, 8};
    // Then
    Assertions
        .assertThatThrownBy(() -> new Draw(numbers, 1000))
        .isInstanceOf(IllegalStateException.class);
  }
}