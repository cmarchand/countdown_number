package top.marchand.java.countdown.number;

import java.util.List;

public interface CountdownGameSolver {
  List<Solution> solve(Draw draw);

  record Solution (String expression) {
  }
}
