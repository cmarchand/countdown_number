package top.marchand.java.countdown.number.impl;

import top.marchand.java.countdown.number.CountdownGameSolver;
import top.marchand.java.countdown.number.Draw;

import java.util.*;
import java.util.function.IntBinaryOperator;

public class NaiveCountdownGameSolver implements CountdownGameSolver {
  @Override
  public List<Solution> solve(Draw draw) {
    // copie défensive
    int[] numbers = Arrays.copyOf(draw.numbers(), draw.numbers().length);
    List<List<Integer>> permutedNumbers = Permuter.permute(numbers);
    Set<List<Operator>> operatorsPermutations = buildOperatorsPermutations();
    List<Solution> solutions = new ArrayList<>();
    for(List<Integer> operands: permutedNumbers) {
      for(List<Operator> operators: operatorsPermutations) {
        StringJoiner joiner = null;
        try {
          joiner = new StringJoiner(" ");
          int result = operands.getFirst();
          joiner.add(Integer.toString(result));
          for (int i = 0; i < 5; i++) {
            Operator operator = operators.get(i);
            joiner.add(operator.symbol());
            Integer right = operands.get(i + 1);
            joiner.add(Integer.toString(right));
            result = operator.calculate(result, right);
            if (result == draw.target()) {
              solutions.add(new Solution(joiner.toString()));
            }
          }
        } catch (CalculationException e) {
        }
      }
    }
    return solutions;
  }

  protected Set<List<Operator>> buildOperatorsPermutations() {
    Set<List<Operator>> operatorsPermutations = new HashSet<>();
    for (int i = 0; i < 4; i++) {
      for(int j = 0; j < 4; j++) {
        for(int k = 0; k < 4; k++) {
          for (int l = 0; l < 4; l++) {
            for (int m = 0; m < 4; m++) {
              operatorsPermutations.add(List.of(
                  Operator.values()[i],
                  Operator.values()[j],
                  Operator.values()[k],
                  Operator.values()[l],
                  Operator.values()[m]));
            }
          }
        }
      }
    }
    return operatorsPermutations;
  }

  protected enum Operator {
    PLUS(
        "+",
        Integer::sum,
        (_, _, _) -> {
        }),
    MINUS(
        "-",
        (integer, integer2) -> integer - integer2,
        (left, right, _) -> {
          if(right > left) throw new NegativeResultException(left+"-"+right);
          if(right == left) throw new NullResultException(left+"-"+right+ " is null");
        }),
    MULTIPLY(
        "*",
        (integer, integer2) -> integer * integer2,
        (left, right, _) -> {
          if(left == 1 || right == 1) throw new UnNecessaryCalculationException(left+"*"+right);
        }),
    DIVIDE(
        "/",
        (integer, integer2) -> integer / integer2,
        (left, right, ret) -> {
          if(left != ret * right) throw new NotAnNaturalException(left+"/"+right);
        }),
    ;

    private final IntBinaryOperator calculator;
    private final String symbol;
    private final CalculChecker checker;

    Operator(String symbol, IntBinaryOperator calculator, CalculChecker checker) {
      this.calculator = calculator;
      this.symbol = symbol;
      this.checker = checker;
    }

    public int calculate(int left, int right) {
      int ret = calculator.applyAsInt(left, right);
      checkResult(left, right, ret);
      return (int) ret;
    }

    private void checkResult(int left, int right, int ret) {
      checker.check(left, right, ret);
    }

    public String symbol() {
      return symbol;
    }

  }
  private static abstract sealed class CalculationException extends RuntimeException permits NegativeResultException, NotAnNaturalException, NullResultException, UnNecessaryCalculationException {
    CalculationException(String s) {super(s);}
  }
  private static final class NotAnNaturalException extends CalculationException {
    public NotAnNaturalException(String s) {
      super(s);
    }
  }

  private static final class UnNecessaryCalculationException extends CalculationException {
    public UnNecessaryCalculationException(String s) {
      super(s);
    }
  }

  private static final class NegativeResultException extends CalculationException {
    public NegativeResultException(String s) {
      super(s);
    }
  }

  private static final class NullResultException extends CalculationException {
    public NullResultException(String s) {
      super(s);
    }
  }

  @FunctionalInterface
  interface CalculChecker { void check(int left, int right, int ret); }

  public static void main(String[] args) {
    Draw draw = new Draw(new int[]{75, 1, 6, 7, 8, 10}, 369);
    new NaiveCountdownGameSolver().solve(draw);
  }
}
